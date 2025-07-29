package com.DreamFactory.DF.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuthServiceHelperTest {

    @InjectMocks
    private AuthServiceHelper authServiceHelper;

    @BeforeEach
    void setUp() {
    }

    @Test
    void generateAccessTokenTest() {
        Claims claims = Jwts.claims()
                .add("username", "user")
                .build();

        String token = authServiceHelper.generateAccessToken("user", claims);

        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
    }

    @Test
    void generateRefreshTokenTest() {
        String refreshToken = authServiceHelper.generateRefreshToken("user");

        assertNotNull(refreshToken);
        assertTrue(refreshToken.length() > 100);
    }

    @Test
    void validateAccessTokenTest() {
        Claims claims = Jwts.claims()
                .add("username", "user")
                .build();

        String token = authServiceHelper.generateAccessToken("user", claims);
        Claims claimsResult = authServiceHelper.validateAccessToken(token);

        assertEquals("user", claimsResult.getSubject());
    }

    @Test
    void validateRefreshTokenTest() {
        String refreshToken = authServiceHelper.generateRefreshToken("user");
        Claims claimsResult = authServiceHelper.validateRefreshToken(refreshToken);

        assertEquals("user", claimsResult.getSubject());
    }

    @Nested
    class handleRefreshTokenTest {
        @Test
        void handleRefreshTokenTest_return_valid() {
            String refreshToken = authServiceHelper.generateRefreshToken("user");
            ResponseEntity<Map<String, String>> response = authServiceHelper.handleRefreshToken(refreshToken);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().get("accessToken"));
        }

        @Test
        void handleRefreshTokenTest_return_invalid() {
            ResponseEntity<Map<String, String>> response = authServiceHelper.handleRefreshToken("invalid-refresh-token");

            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertEquals("Refresh invalid token", response.getBody().get("error"));

        }

        @Test
        void when_handleRefreshTokenIsBlank_return_noRefreshTokenProvided() {
            ResponseEntity<Map<String, String>> response = authServiceHelper.handleRefreshToken("");

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("No refresh token provided", response.getBody().get("error"));
        }

        @Test
        void when_handleRefreshTokenIsNull_return_noRefreshTokenProvided() {
            ResponseEntity<Map<String, String>> response = authServiceHelper.handleRefreshToken(null);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("No refresh token provided", response.getBody().get("error"));
        }


    }

}