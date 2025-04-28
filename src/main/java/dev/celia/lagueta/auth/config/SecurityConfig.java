package dev.celia.lagueta.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/products/with-image").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/reviews/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/images/**").permitAll()
                    .requestMatchers("/api/auth/login").permitAll()
                    .anyRequest().permitAll()
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
