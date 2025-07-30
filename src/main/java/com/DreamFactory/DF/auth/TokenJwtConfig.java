package com.DreamFactory.DF.auth;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class TokenJwtConfig {
    public static final String prefixToken = "Bearer ";
    public static final String headerAuthorization = "Authorization";
    public  static final String contentType = "application/json";
    public static final SecretKey secretKey = Jwts.SIG.HS256.key().build();
}
