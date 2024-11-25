package com.rustam.api_gateway.config;

import com.rustam.api_gateway.filter.JwtAuthenticationFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private final JwtAuthenticationFilter filter;

    public GatewayConfig(JwtAuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ms-account", r -> r.path("/api/v1/account/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ms-account"))

                .route("ms-transfer", r -> r.path("/api/v1/transfer/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://ms-transfer"))

                .route("ms-auth", r -> r.path("/api/v1/auth/**")
                        .uri("lb://ms-auth"))
                .build();
    }
}
