package com.rustam.ms_account.util.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.getTokenFromRequest(request);
        String refreshToken = "";
        if (token != null){
            if (!jwtUtil.isValidUserIdFromToken(token)) {
                refreshToken = token;
                Map<String, String> requestBody = new HashMap<>();
                requestBody.put("refreshToken", refreshToken);

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

                ResponseEntity<String> restResponse = restTemplate.exchange(
                        "http://localhost:8085/api/v1/auth/refresh-token",
                        HttpMethod.POST,
                        entity,
                        String.class
                );

                if (restResponse.getStatusCode().equals(HttpStatus.OK)){
                    refreshToken = restResponse.getBody();
                }
            }else {
                refreshToken = token;
            }
        }
        jwtUtil.validateToken(refreshToken);

        filterChain.doFilter(request, response);
    }
}
