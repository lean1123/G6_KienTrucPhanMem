package ktpm17ctt.g6.identity.service.implement;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import ktpm17ctt.g6.identity.dto.request.AuthenticationRequest;
import ktpm17ctt.g6.identity.dto.request.IntrospectRequest;
import ktpm17ctt.g6.identity.dto.request.LogoutRequest;
import ktpm17ctt.g6.identity.dto.request.RefreshRequest;
import ktpm17ctt.g6.identity.dto.response.AuthenticationResponse;
import ktpm17ctt.g6.identity.dto.response.IntrospectResponse;
import ktpm17ctt.g6.identity.entity.Account;
import ktpm17ctt.g6.identity.exception.AppException;
import ktpm17ctt.g6.identity.exception.ErrorCode;
import ktpm17ctt.g6.identity.repository.AccountRepository;
import ktpm17ctt.g6.identity.service.AuthenticationService;
import ktpm17ctt.g6.identity.service.RefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    AccountRepository accountRepository;
    StringRedisTemplate redisTemplate;
    RefreshTokenService refreshTokenService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;

    @Override
    public IntrospectResponse introspect(IntrospectRequest request) {
        var token = request.getToken();
        boolean isValid = true;

        try {
                verifyToken(token);
        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        var account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(account);

        refreshTokenService.createRefreshToken(account, token.refreshJti, token.refreshToken);

        return AuthenticationResponse.builder()
                .token(token.accessToken)
                .expiryTime(token.accessTokenExpiry)
                .refreshToken(token.refreshToken)
                .refreshTokenExpiryTime(token.refreshTokenExpiry)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = verifyToken(request.getToken());
        var refreshToken = verifyToken(request.getRefreshToken());
        String refreshJti = refreshToken.getJWTClaimsSet().getJWTID();
        refreshTokenService.revokeRefreshToken(refreshJti);
        redisTemplate.delete("token:" + request.getToken());
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getRefreshToken());

        var jti = signedJWT.getJWTClaimsSet().getJWTID();

        var oldToken = refreshTokenService.validateRefreshToken(jti);

        refreshTokenService.invalidateRefreshToken(oldToken.getId());

        var email = signedJWT.getJWTClaimsSet().getSubject();

        var account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(account);
        refreshTokenService.createRefreshToken(account, token.refreshJti, token.refreshToken);

        return AuthenticationResponse.builder()
                .token(token.accessToken)
                .expiryTime(token.accessTokenExpiry())
                .refreshToken(token.refreshToken)
                .refreshTokenExpiryTime(token.refreshTokenExpiry())
                .build();
    }

    private TokenPair generateToken(Account account) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        Date issueTime = new Date();
        Date accessTokenExpiry = new Date(Instant.ofEpochMilli(issueTime.getTime()).plus(15, ChronoUnit.MINUTES).toEpochMilli());
        Date refreshTokenExpiry = new Date(Instant.ofEpochMilli(issueTime.getTime()).plus(7, ChronoUnit.DAYS).toEpochMilli());

        String accessJti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();

        JWTClaimsSet accessClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issuer("identity-service")
                .issueTime(issueTime)
                .expirationTime(accessTokenExpiry)
                .jwtID(accessJti)
                .claim("scope", buildScope(account))
                .claim("accountId", account.getId())
                .build();

        JWTClaimsSet refreshClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .issuer("identity-service")
                .issueTime(issueTime)
                .expirationTime(refreshTokenExpiry)
                .jwtID(refreshJti)
                .claim("type", "refresh_token")
                .build();

        SignedJWT accessJWT = new SignedJWT(header, accessClaimsSet);
        SignedJWT refreshSignedJWT = new SignedJWT(header, refreshClaimsSet);

        try {
            accessJWT.sign(new MACSigner(signerKey.getBytes()));
            refreshSignedJWT.sign(new MACSigner(signerKey.getBytes()));
            return new TokenPair(
                    accessJti,
                    refreshJti,
                    accessJWT.serialize(),
                    refreshSignedJWT.serialize(),
                    accessTokenExpiry,
                    refreshTokenExpiry
            );
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);
        return signedJWT;
    }

    private String buildScope(Account account) {
        StringJoiner joiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(account.getRoles())) {
            account.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());
            });
        }

        return joiner.toString();
    }

    private record TokenPair(
            String accessJti,
            String refreshJti,
            String accessToken,
            String refreshToken,
            Date accessTokenExpiry,
            Date refreshTokenExpiry
    ) { }
}
