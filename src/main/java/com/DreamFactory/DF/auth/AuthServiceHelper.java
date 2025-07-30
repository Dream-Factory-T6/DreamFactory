package com.DreamFactory.DF.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.DreamFactory.DF.auth.TokenJwtConfig.secretKey;

@Service
public class AuthServiceHelper {
    public String generateAccessToken(String username, Claims claims) {
        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .signWith(secretKey)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .compact();
        return token;
    }

    public String generateRefreshToken(String username) {
        String refreshToken = Jwts.builder()
                .subject(username)
                .signWith(secretKey)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 604800000)) // 7 días
                .compact();
        return refreshToken;
    }

    public Claims validateAccessToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims validateRefreshToken(String refreshToken) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(refreshToken)
                .getPayload();
    }

    public ResponseEntity<Map<String, String>> handleRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "No refresh token provided"));
        }
        try {
            Claims claims = validateRefreshToken(refreshToken);

            String username = claims.getSubject();

            String newAccessToken = generateAccessToken(username, claims);

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Refresh invalid token"));
        }
    }

    public Authentication getAuthentication(String token) {
        try {
            Claims claims = validateAccessToken(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}
