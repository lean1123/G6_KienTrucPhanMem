package ktpm17ctt.g6.identity.service.implement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import ktpm17ctt.g6.identity.dto.request.*;
import ktpm17ctt.g6.identity.dto.response.AuthenticationResponse;
import ktpm17ctt.g6.identity.dto.response.IntrospectResponse;
import ktpm17ctt.g6.identity.entity.Account;
import ktpm17ctt.g6.identity.entity.Role;
import ktpm17ctt.g6.identity.exception.AppException;
import ktpm17ctt.g6.identity.exception.ErrorCode;
import ktpm17ctt.g6.identity.mapper.UserMapper;
import ktpm17ctt.g6.identity.repository.AccountRepository;
import ktpm17ctt.g6.identity.repository.RoleRepository;
import ktpm17ctt.g6.identity.repository.httpClient.UserClient;
import ktpm17ctt.g6.identity.service.AuthenticationService;
import ktpm17ctt.g6.identity.service.RefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    AccountRepository accountRepository;
    RoleRepository roleRepository;
    StringRedisTemplate redisTemplate;
    RefreshTokenService refreshTokenService;
    UserMapper userMapper;
    UserClient userClient;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String signerKey;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.client-id}")
    protected String clientId;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.client-secret}")
    protected String clientSecret;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.redirect-uri}")
    protected String redirectUri;

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
                .role(token.role)
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
                .role(token.role)
                .build();
    }

    @Override
    public AuthenticationResponse loginSocial(LoginSocialRequest request, String provider) throws ParseException, JOSEException {
        if (request.getGoogleAccountId() == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (provider.toLowerCase().equals("google")) {
            var account = accountRepository.findByGoogleAccountId(request.getGoogleAccountId());
            if (account.isPresent()) {
                return getAuthenticationResponse(account.get());
            }
            var optionalAccount = accountRepository.findByEmail(request.getEmail());
            if (optionalAccount.isPresent()) {
                var tempAccount = optionalAccount.get();
                tempAccount.setGoogleAccountId(request.getGoogleAccountId());
                accountRepository.save(tempAccount);
                return getAuthenticationResponse(tempAccount);
            }
            var tempAccount = Account.builder()
                    .email(request.getEmail())
                    .googleAccountId(request.getGoogleAccountId())
                    .build();
            HashSet<Role> roles = new HashSet<>();
            roleRepository.findById("USER").ifPresent(roles::add);
            tempAccount.setRoles(roles);

            var savedAccount = accountRepository.save(tempAccount);
            var userCreationRequest = userMapper.toUserCreationRequest(request);
            userCreationRequest.setAccountId(savedAccount.getId());
            var userProfile = userClient.createProfile(userCreationRequest);
            return getAuthenticationResponse(savedAccount);
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
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
                    refreshTokenExpiry,
                account.getRoles().stream()
                        .map(role -> role.getName())
                        .toList().toString()
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
            Date refreshTokenExpiry,
            String role
    ) { }

    @Override
    public String generateSocialAuthenticationURL(String provider) {
        provider = provider.trim().toLowerCase();
        if (provider.equals("google")) {
            return String.format("https://accounts.google.com/o/oauth2/v2/auth?client_id=%s&redirect_uri=%s&response_type=code&scope=openid%%20email%%20profile", clientId, redirectUri);
        }
        return "";
    }

    private MultiValueMap<String, String> getTokenParams(String code) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("scope", "openid email profile");
        params.add("code", code);
        params.add("grant_type", "authorization_code");
        return params;
    }

    private void validateTokenResponse(Map<String, Object> tokenResponse) {
        if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private AuthenticationResponse getAuthenticationResponse(Account account) {
        var token = generateToken(account);
        refreshTokenService.createRefreshToken(account, token.refreshJti, token.refreshToken);
        return AuthenticationResponse.builder()
                .token(token.accessToken)
                .expiryTime(token.accessTokenExpiry)
                .refreshToken(token.refreshToken)
                .refreshTokenExpiryTime(token.refreshTokenExpiry)
                .role(token.role)
                .build();
    }

    @Override
    public Map<String, Object> authenticationAndFetchProfile(String provider, String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        String accessToken;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (provider.equals("google")) {
            String tokenUrl = "https://oauth2.googleapis.com/token";
            MultiValueMap<String, String> params = getTokenParams(code);
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
            Map<String, Object> tokenResponse = objectMapper.readValue(response.getBody(), Map.class);
            validateTokenResponse(tokenResponse);
            accessToken = (String) tokenResponse.get("access_token");
            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);
            ResponseEntity<String> userInfoResponse = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    new HttpEntity<>(userInfoHeaders),
                    String.class
            );
            Map<String, Object> userInfo = objectMapper.readValue(userInfoResponse.getBody(), Map.class);
            if (userInfo.containsKey("error")) {
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
            return userInfo;
        } else {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}
