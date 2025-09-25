package com.unifacisa.linkedin.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    private SecretKey getSigningKey() {
        if (secret == null) {
            System.err.println("!!!!!!!!!! (jwt.secret) ESTÁ NULA !!!!!!!!!!");
            throw new IllegalStateException("A chave secreta JWT não pode ser nula.");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UsuarioDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", userDetails.getAuthorities().toString().replaceAll("]", "").replaceAll("\\[", ""));
        claims.put("userID", userDetails.getId().toString());
        claims.put("email", userDetails.getEmail());


        String token = createToken(claims, userDetails.getEmail());

        return token;
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10 horas
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        // --- INÍCIO DO BLOCO DE DEBUG ---
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            System.err.println("!!!!!!!!!! FALHA NA VALIDAÇÃO DO JWT !!!!!!!!!!");
            System.err.println(" Assinatura Inválida!");
            System.err.println("Mensagem: " + e.getMessage());
            throw e;
        } catch (ExpiredJwtException e) {
            System.err.println("!!!!!!!!!! FALHA NA VALIDAÇÃO DO JWT !!!!!!!!!!");
            System.err.println("Token Expirado!");
            System.err.println("Mensagem: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("!!!!!!!!!! FALHA NA VALIDAÇÃO DO JWT !!!!!!!!!!");
            System.err.println("Tipo da Exceção: " + e.getClass().getName());
            System.err.println("Mensagem: " + e.getMessage());
            throw e;
        }
        // --- FIM DO BLOCO DE DEBUG ---
    }

    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    public Boolean validateToken(String token, UsuarioDetails userDetails) {
        try {
            final String username = extractUsername(token);
            return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) { return extractClaim(token, Claims::getSubject); }
    public Date extractExpiration(String token) { return extractClaim(token, Claims::getExpiration); }
}