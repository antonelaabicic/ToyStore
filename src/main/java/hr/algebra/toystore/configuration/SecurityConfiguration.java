package hr.algebra.toystore.configuration;

import hr.algebra.toystore.security.JwtAuthFilter;
import hr.algebra.toystore.util.Constants;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;

    @SuppressWarnings("java:S4502")
    @Bean
    @Order(1)
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/refresh",
                                "/api/demo/sql/**", "/api/demo/ssrf/**", "/api/demo/deserialization/**")
                        .permitAll()

                        .requestMatchers("/api/auth/revoke/**")
                        .hasRole(Constants.ADMIN)

                        .requestMatchers("/api/auth/logout")
                        .authenticated()

                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, e) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("""
                            {
                              "error":"Unauthorized"
                            }
                            """);
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            res.setContentType("application/json");
                            res.getWriter().write("""
                            {
                              "error":"Forbidden"
                            }
                            """);
                        })
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @SuppressWarnings("java:S4502")
    @Bean
    @Order(2)
    SecurityFilterChain mvcSecurity(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        "/h2-console/**"
                ))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/toystore/**", "/store/**", "/cart/**", "/images/**", "/css/**",
                                "/js/**", "/user/**", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**"
                                )
                        .permitAll()

                        .requestMatchers("/orders/**")
                        .hasAnyRole(Constants.USER, Constants.ADMIN)

                        .requestMatchers("/admin/**")
                        .hasRole(Constants.ADMIN)

                        .anyRequest()
                        .authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/user/login")
                        .loginProcessingUrl("/user/login")
                        .defaultSuccessUrl("/orders/checkout", true)
                        .permitAll()
                )

                .logout(LogoutConfigurer::permitAll)

                .headers(headers ->
                        headers.frameOptions(
                                        HeadersConfigurer.FrameOptionsConfig::sameOrigin
                                )
                                .contentSecurityPolicy(csp ->
                                        csp.policyDirectives(
                                                "default-src 'self'; " +
                                                "script-src 'self' 'unsafe-inline'; " +
                                                "style-src 'self' 'unsafe-inline'; " +
                                                "img-src 'self' data: blob:; " +
                                                "font-src 'self'; " +
                                                "connect-src 'self'; " +
                                                "form-action 'self'; " +
                                                "object-src 'none'; " +
                                                "base-uri 'self'; " +
                                                "frame-ancestors 'self';"
                                        )
                                )
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
