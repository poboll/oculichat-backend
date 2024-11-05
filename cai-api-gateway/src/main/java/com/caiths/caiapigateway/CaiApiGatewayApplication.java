package com.caiths.caiapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CaiApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(CaiApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("to_baidu", r -> r.path("/baidu")
                        .uri("http://www.baidu.com/"))
                .route("to_caiths", r -> r.path("/caiths")
                        .uri("http://www.caiths.com/"))
                .build();
    }

}
