package com.fse.FSE_Backend_Proj.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secret:mySecretKey12345678901234567890123456789012}") // Must be 32+ characters
    private String secret;

    @Value("${jwt.expiration:86400000}") // 1 day in milliseconds
    private long expirationTime;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ✅ Combines email and role into subject
    public String generateToken(String email, String role) {
        String subject = email + ":" + role;  // Combined format
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extracts "email:ROLE" (full subject)
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Optionally extract email from subject
    public String extractEmail(String token) {
        String subject = extractUsername(token);
        return subject.split(":")[0]; // before ":"
    }

    // ✅ Optionally extract role from subject
    public String extractRole(String token) {
        String subject = extractUsername(token);
        return subject.split(":")[1]; // after ":"
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
