package ktpm17ctt.g6.identity.service;

import ktpm17ctt.g6.identity.entity.Account;
import ktpm17ctt.g6.identity.entity.RefreshToken;

public interface RefreshTokenService {
    void createRefreshToken(Account account, String refreshJti, String token);
    void invalidateRefreshToken(String jti);
    void revokeRefreshToken(String jti);
    RefreshToken validateRefreshToken(String jti);
}
