package com.DreamFactory.DF.config;

import com.DreamFactory.DF.auth.AuthServiceHelper;
import com.DreamFactory.DF.auth.filter.JwtAuthenticationFilter;
import com.DreamFactory.DF.auth.filter.JwtValidationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private AuthServiceHelper authServiceHelper;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "X-Requested-With", "Accept",
                "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authz -> authz
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/ws/**", "/chat.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/destinations/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/reviews", "/api/destinations/**")
                .hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/destinations/**")
                .hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/destinations/**")
                .hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/**")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/register/admin")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/**")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**")
                .hasRole("ADMIN")
                .requestMatchers("/api/chat/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()

        )
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), authServiceHelper))
                .addFilter(new JwtValidationFilter(authenticationManager(), authServiceHelper))
                .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
