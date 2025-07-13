package com.DreamFactory.DF.config;

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

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.GET, "/api/destinations/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/reviews/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/reviews", "/api/destinations/**")
                .hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/destinations/**")
                .hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/destinations/**")
                .hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/api/users")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/users/**")
                .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/users/**")
                .hasRole("ADMIN")
                .anyRequest().authenticated()

        )
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
