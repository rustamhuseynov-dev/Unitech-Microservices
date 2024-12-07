package com.rustam.api_gateway.filter;

import com.rustam.api_gateway.dto.RefreshRequest;
import com.rustam.api_gateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter implements GatewayFilter {

    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        final List<String> apiEndpoints = List.of("/api/v1/auth/**", "/eureka");

        Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
                .noneMatch(uri -> r.getURI().getPath().contains(uri));

        if (isApiSecured.test(request)) {
            if (authMissing(request)) return onError(exchange);

            String token = request.getHeaders().getOrEmpty("Authorization").get(0);

            if (token != null && token.startsWith("Bearer ")) token = token.substring(7);

            try {
                String refreshToken = "";
                if (token != null){
                    if (!jwtUtil.isValidUserIdFromToken(token)) {
                        refreshToken = token;
                        Map<String, String> requestBody = new HashMap<>();
                        requestBody.put("refreshToken", refreshToken);

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

                        ResponseEntity<String> response = restTemplate.exchange(
                                "http://localhost:8085/api/v1/auth/refresh-token",
                                HttpMethod.POST,
                                entity,
                                String.class
                        );

                        if (response.getStatusCode().equals(HttpStatus.OK)){
                            refreshToken = response.getBody();
                        }
                    }else {
                        refreshToken = token;
                    }
                }
                jwtUtil.validateToken(refreshToken);
            } catch (Exception e) {
                return onError(exchange);
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean authMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }
}
