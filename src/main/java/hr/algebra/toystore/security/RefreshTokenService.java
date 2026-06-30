package hr.algebra.toystore.security;

import hr.algebra.toystore.model.ApplicationUser;
import hr.algebra.toystore.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(ApplicationUser user);
    Optional<RefreshToken> findByToken(String token);
    void verifyExpiration(RefreshToken token);
    void revokeAllUserTokens(Integer userId);
    RefreshToken rotateRefreshToken(RefreshToken oldToken);
}
