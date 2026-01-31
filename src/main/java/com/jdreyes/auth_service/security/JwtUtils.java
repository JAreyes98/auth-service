package com.jdreyes.auth_service.security;

import java.security.Key;
import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .claim("type", "USER") 
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTokenForExternalApp(String clientId, Set<String> scopes) {
        return Jwts.builder()
                .setSubject(clientId)
                .claim("scopes", scopes)
                .claim("type", "EXTERNAL_APP")
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + (jwtExpirationMs * 24)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
