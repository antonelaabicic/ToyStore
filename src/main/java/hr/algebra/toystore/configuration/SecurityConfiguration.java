package hr.algebra.toystore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionFixation().none()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//        System.out.println(encoder.encode("123"));
//
//        boolean matches = encoder.matches("123", "$2a$10$HDaMrzUr4V5I6GfwXa0kxuXW7sMUk8bF/HBmY3ygvSymqsJk8Cx7C");
//        System.out.println("Match? " + matches);
//    }
}
