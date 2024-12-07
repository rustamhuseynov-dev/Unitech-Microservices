package com.rustam.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    @Value("${spring.application.jwt.secret-key}")
    public String SECRET ;
    

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }
    
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isValidUserIdFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();

            // User ID və ya username-i əldə edin
            String userId = claims.getSubject();

            // Burada userId yoxlayın (null və ya boş deyil)
            return userId != null && !userId.isEmpty();
        } catch (ExpiredJwtException e) {
            // Token vaxtı keçmişdir
            return false; // Burada false qaytarılır
        } catch (SignatureException | IllegalArgumentException e) {
            // Token səhvdir
            return false; // Burada false qaytarılır
        }
    }
}
