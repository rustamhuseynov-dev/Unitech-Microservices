package com.rustam.api_gateway.config;

import com.rustam.api_gateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ms-account", r -> r.path("/api/v1/account/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ms-account"))

                .route("ms-currency", r -> r.path("/api/v1/currency/**")
                        .filters(f -> f.filter(filter))
                        .uri("http://localhost:8081"))

                .route("ms-auth", r -> r.path("/api/v1/auth/**")
                        .uri("lb://unit"))
                .build();
    }
}
