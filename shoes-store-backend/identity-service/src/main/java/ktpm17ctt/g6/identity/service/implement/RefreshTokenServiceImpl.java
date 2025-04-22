package ktpm17ctt.g6.identity.service.implement;

import ktpm17ctt.g6.identity.entity.Account;
import ktpm17ctt.g6.identity.entity.RefreshToken;
import ktpm17ctt.g6.identity.entity.enums.TokenStatus;
import ktpm17ctt.g6.identity.exception.AppException;
import ktpm17ctt.g6.identity.exception.ErrorCode;
import ktpm17ctt.g6.identity.repository.RefreshTokenRepository;
import ktpm17ctt.g6.identity.service.RefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
    RefreshTokenRepository refreshTokenRepository;

    @Override
    public void createRefreshToken(Account account, String refreshJti, String token) {
        var refreshToken = RefreshToken.builder()
                .id(refreshJti)
                .token(token)
                .account(account)
                .expirationDate(Date.from(Instant.now().plus(7, ChronoUnit.DAYS)))
                .status(TokenStatus.VALID)
                .build();
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void invalidateRefreshToken(String jti) {
        refreshTokenRepository.findById(jti).ifPresent(rt -> {
            rt.setStatus(TokenStatus.USED);
            refreshTokenRepository.save(rt);
        });
    }

    @Override
    public void revokeRefreshToken(String jti) {
        refreshTokenRepository.findById(jti).ifPresent(rt -> {
            rt.setStatus(TokenStatus.REVOKED);
            refreshTokenRepository.save(rt);
        });
    }

    @Override
    public RefreshToken validateRefreshToken(String jti) {
        return refreshTokenRepository.findById(jti)
                .filter(rt -> rt.getStatus() == TokenStatus.VALID)
                .filter(rt -> rt.getExpirationDate().after(Date.from(Instant.now())))
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    }
}
