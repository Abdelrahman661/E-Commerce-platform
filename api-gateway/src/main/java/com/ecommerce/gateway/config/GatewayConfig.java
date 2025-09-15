package com.ecommerce.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/auth/**")
                        .uri("http://localhost:8081")) // Auth Service
                .route("product-service", r -> r.path("/products/**")
                        .uri("http://localhost:8082")) // Product Service
                .route("order-service", r -> r.path("/orders/**")
                        .uri("http://localhost:8083")) // Order Service
                .build();
    }
}