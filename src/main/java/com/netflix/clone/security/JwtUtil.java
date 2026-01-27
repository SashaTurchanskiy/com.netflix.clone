package com.netflix.clone.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final long JWT_TOKEN_VALIDITY = 30L * 24 * 60 * 60 * 1000; // 30 days in milliseconds

    @Value("${jwt.secret:qweusdfmnspdfkwoiejfoisvjbfoidfvpiodjnoibfmdfpobdpobkfofpzsfpsl}")
    private String secret;

    @Value("1800000")
    private long accessTokenExpiration;

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }
    public String extractEmail(String token){
        return getClaimFromToken(token, claims -> claims.get("email", String.class));
    }
    public String getRoleFromToken(String token){
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }
    public String extractTokenType(String token){
        return getClaimFromToken(token, claims -> claims.get("type", String.class));
    }
    public Date extractExpiration(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    private Boolean isTokenExpired(String token) {
       return extractExpiration(token).before(new Date());
    }
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);
        return doGenerateToken(claims, username, accessTokenExpiration);
    }
    private String doGenerateToken(Map<String, Object> claims, String subject, long accessTokenExpiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public Boolean isAccessToken(String token){
        return "ACCESS".equals(extractTokenType(token));
    }

    public Boolean validateToken(String token, String email){
        final String extractEmail = extractEmail(token);
        return (extractEmail.equals(email) && !isTokenExpired(token));
        }
    }

