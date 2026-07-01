package hr.algebra.toystore.service;

import hr.algebra.toystore.model.ApplicationUser;
import hr.algebra.toystore.model.RefreshToken;
import hr.algebra.toystore.repository.RefreshTokenRepository;
import hr.algebra.toystore.security.RefreshTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField( refreshTokenService, "refreshTokenExpirationDays",7L);
    }

    @Test
    @DisplayName("createRefreshToken -> creates and saves a new refresh token")
    void createRefreshToken() {
        ApplicationUser user = ApplicationUser.builder()
                .id(1)
                .username("ninic")
                .build();

        when(refreshTokenRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken token = refreshTokenService.createRefreshToken(user);

        verify(refreshTokenRepository).save(any(RefreshToken.class));

        assertEquals(user, token.getUser());
        assertFalse(token.isRevoked());
        assertNotNull(token.getToken());
        assertNotNull(token.getCreatedAt());
        assertNotNull(token.getExpiryDate());
    }

    @Test
    @DisplayName("findByToken -> returns token")
    void findByToken() {
        RefreshToken token = RefreshToken.builder()
                .token("abc")
                .build();

        when(refreshTokenRepository.findByToken("abc")).thenReturn(Optional.of(token));

        Optional<RefreshToken> result = refreshTokenService.findByToken("abc");

        assertTrue(result.isPresent());
        assertEquals(token, result.get());

        verify(refreshTokenRepository).findByToken("abc");
    }

    @Test
    @SuppressWarnings("java:S8692")
    @DisplayName("verifyExpiration -> valid token does nothing")
    void verifyExpirationValid() {
        RefreshToken token = RefreshToken.builder()
                .revoked(false)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();

        assertDoesNotThrow(() -> refreshTokenService.verifyExpiration(token));

        verify(refreshTokenRepository, never()).save(any());
    }

    @Test
    @SuppressWarnings("java:S8692")
    @DisplayName("verifyExpiration -> expired token is revoked")
    void verifyExpirationExpired() {
        RefreshToken token = RefreshToken.builder()
                .revoked(false)
                .expiryDate(LocalDateTime.now().minusDays(1))
                .build();

        when(refreshTokenRepository.save(any())).thenReturn(token);

        IllegalArgumentException ex =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> refreshTokenService.verifyExpiration(token));

        assertEquals("Refresh token has expired.", ex.getMessage());
        assertTrue(token.isRevoked());

        verify(refreshTokenRepository).save(token);
    }

    @Test
    @DisplayName("verifyExpiration -> revoked token detects reuse attack")
    void verifyExpirationReuseAttack() {
        ApplicationUser user = ApplicationUser.builder()
                .id(5)
                .build();

        RefreshToken token = RefreshToken.builder()
                .user(user)
                .revoked(true)
                .build();

        ResponseStatusException ex =
                assertThrows(ResponseStatusException.class,
                        () -> refreshTokenService.verifyExpiration(token));

        assertEquals(HttpStatus.UNAUTHORIZED, ex.getStatusCode());
        assertEquals("Refresh token reuse detected.", ex.getReason());

        verify(refreshTokenRepository).revokeAllByUserId(5);
    }

    @Test
    @DisplayName("revokeAllUserTokens -> revokes every token")
    void revokeAllUserTokens() {
        refreshTokenService.revokeAllUserTokens(3);
        verify(refreshTokenRepository).revokeAllByUserId(3);
    }

    @Test
    @DisplayName("rotateRefreshToken -> creates new token and revokes old")
    void rotateRefreshToken() {
        ApplicationUser user = ApplicationUser.builder()
                .id(1)
                .build();

        RefreshToken oldToken = RefreshToken.builder()
                .user(user)
                .token("old")
                .revoked(false)
                .build();

        when(refreshTokenRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken newToken = refreshTokenService.rotateRefreshToken(oldToken);

        assertTrue(oldToken.isRevoked());
        assertNotNull(oldToken.getReplacedByToken());
        assertEquals(user, newToken.getUser());
        assertFalse(newToken.isRevoked());

        verify(refreshTokenRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("rotateRefreshToken -> generates different token")
    void rotateRefreshTokenDifferentValue() {
        ApplicationUser user = ApplicationUser.builder()
                .id(1)
                .build();

        RefreshToken oldToken = RefreshToken.builder()
                .user(user)
                .token("old")
                .build();

        when(refreshTokenRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RefreshToken newToken = refreshTokenService.rotateRefreshToken(oldToken);
        assertNotEquals(oldToken.getToken(), newToken.getToken());
    }
}
