package com.rustam.ms_account.util.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtUtil {

    @Value("${spring.application.jwt}")
    private String secretKey;

    public String extractSubject(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }
}
