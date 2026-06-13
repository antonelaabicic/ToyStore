package hr.algebra.toystore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    SecurityFilterChain web(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/toystore/**", "/store/**", "/cart/**", "/images/**",
                                "/css/**", "/js/**", "/user/**"
                        ).permitAll()

                        .requestMatchers("/orders/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .defaultSuccessUrl("/orders/checkout", true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                                .contentSecurityPolicy(csp ->
                                        csp.policyDirectives(
                                                "default-src 'self'; " +
                                                "script-src 'self'; " +
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
}
