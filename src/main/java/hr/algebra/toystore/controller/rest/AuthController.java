package hr.algebra.toystore.controller.rest;

import hr.algebra.toystore.dto.JwtResponseDto;
import hr.algebra.toystore.dto.LoginRequestDto;
import hr.algebra.toystore.dto.RefreshTokenRequestDto;
import hr.algebra.toystore.model.ApplicationUser;
import hr.algebra.toystore.model.RefreshToken;
import hr.algebra.toystore.service.ApplicationUserService;
import hr.algebra.toystore.service.JwtService;
import hr.algebra.toystore.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final ApplicationUserService applicationUserService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public JwtResponseDto login(@RequestBody LoginRequestDto request) {

        Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        if (!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        ApplicationUser user = applicationUserService.findUserByUsername(request.getUsername());
        String accessToken = jwtService.generateAccessToken(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return JwtResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    @PostMapping("/refresh")
    public JwtResponseDto refreshToken(@RequestBody RefreshTokenRequestDto request) {

        RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                        .orElseThrow(() -> new IllegalArgumentException("Refresh token not found."));
        refreshTokenService.verifyExpiration(refreshToken);

        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken);

        ApplicationUser user = newRefreshToken.getUser();
        String accessToken = jwtService.generateAccessToken(user);

        return JwtResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        ApplicationUser user = applicationUserService.findUserByUsername(authentication.getName());
        refreshTokenService.revokeAllUserTokens(user.getId());
        return ResponseEntity.noContent().build();
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/revoke/{userId}")
    public ResponseEntity<Void> revokeUserTokens(@PathVariable Integer userId) {
        refreshTokenService.revokeAllUserTokens(userId);
        return ResponseEntity.noContent().build();
    }
}
