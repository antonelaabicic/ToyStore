package hr.algebra.toystore.security;

import hr.algebra.toystore.model.ApplicationUser;
import hr.algebra.toystore.model.RefreshToken;
import hr.algebra.toystore.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration-days}")
    private long refreshTokenExpirationDays;

    @Override
    public RefreshToken createRefreshToken(ApplicationUser user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(refreshTokenExpirationDays))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public void verifyExpiration(RefreshToken token) {
        if (token.isRevoked()) {
            revokeAllUserTokens(token.getUser().getId());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token reuse detected.");
        }

        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            token.setRevoked(true);
            refreshTokenRepository.save(token);
            throw new IllegalArgumentException("Refresh token has expired.");
        }
    }

    @Override
    public void revokeAllUserTokens(Integer userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
    }

    @Override
    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken oldToken) {
        RefreshToken newToken = RefreshToken.builder()
                .user(oldToken.getUser())
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(refreshTokenExpirationDays))
                .revoked(false)
                .build();

        oldToken.setRevoked(true);
        oldToken.setReplacedByToken(newToken.getToken());

        refreshTokenRepository.save(oldToken);

        return refreshTokenRepository.save(newToken);
    }
}
