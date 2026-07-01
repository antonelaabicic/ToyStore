package hr.algebra.toystore.service;

import hr.algebra.toystore.model.ApplicationRole;
import hr.algebra.toystore.model.ApplicationUser;
import hr.algebra.toystore.security.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", "MDEyMzQ1Njc4OUFCQ0RFRjAxMjM0NTY3ODlBQkNERUY=");
        ReflectionTestUtils.setField(jwtService, "accessTokenExpirationMinutes", 60L);
        jwtService.init();
    }

    @Test
    @DisplayName("generateAccessToken -> creates valid JWT access token")
    void generateAccessToken() {
        ApplicationRole role = new ApplicationRole("ROLE_USER");

        ApplicationUser user = ApplicationUser.builder()
                .username("pperic")
                .roles(List.of(role))
                .build();

        String token = jwtService.generateAccessToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    @DisplayName("extractUsername -> returns username from token")
    void extractUsername() {
        ApplicationRole role = new ApplicationRole("ROLE_USER");

        ApplicationUser user = ApplicationUser.builder()
                .username("pperic")
                .roles(List.of(role))
                .build();

        String token = jwtService.generateAccessToken(user);

        assertEquals("pperic", jwtService.extractUsername(token));
    }

    @Test
    @DisplayName("extractRoles -> returns token roles")
    void extractRoles() {
        ApplicationRole userRole = new ApplicationRole("ROLE_USER");
        ApplicationRole adminRole = new ApplicationRole("ROLE_ADMIN");

        ApplicationUser user = ApplicationUser.builder()
                .username("pperic")
                .roles(List.of(userRole, adminRole))
                .build();

        String token = jwtService.generateAccessToken(user);

        List<String> roles = jwtService.extractRoles(token);

        assertEquals(2, roles.size());
        assertTrue(roles.contains("ROLE_USER"));
        assertTrue(roles.contains("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("isTokenValid -> returns true for matching username")
    void validToken() {
        ApplicationRole role = new ApplicationRole("ROLE_USER");

        ApplicationUser user = ApplicationUser.builder()
                .username("pperic")
                .roles(List.of(role))
                .build();

        String token = jwtService.generateAccessToken(user);
        assertTrue(jwtService.isTokenValid(token, "pperic"));
    }

    @Test
    @DisplayName("isTokenValid -> returns false for different username")
    void invalidUsername() {
        ApplicationRole role = new ApplicationRole("ROLE_USER");

        ApplicationUser user = ApplicationUser.builder()
                .username("pperic")
                .roles(List.of(role))
                .build();

        String token = jwtService.generateAccessToken(user);
        assertFalse(jwtService.isTokenValid(token, "aanic"));
    }

    @Test
    @DisplayName("extractExpiration -> returns future expiration date")
    void extractExpiration() {
        ApplicationRole role = new ApplicationRole("ROLE_USER");

        ApplicationUser user = ApplicationUser.builder()
                .username("pperic")
                .roles(List.of(role))
                .build();

        String token = jwtService.generateAccessToken(user);
        Date expiration = jwtService.extractExpiration(token);

        assertNotNull(expiration);
        assertEquals(expiration, jwtService.extractClaim(token, Claims::getExpiration));
    }

    @Test
    @DisplayName("extractClaim -> returns subject")
    void extractClaim() {
        ApplicationRole role = new ApplicationRole("ROLE_USER");

        ApplicationUser user = ApplicationUser.builder()
                .username("pperic")
                .roles(List.of(role))
                .build();

        String token = jwtService.generateAccessToken(user);
        String username = jwtService.extractClaim(token, io.jsonwebtoken.Claims::getSubject);
        assertEquals("pperic", username);
    }
}