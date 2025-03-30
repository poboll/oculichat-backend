package com.caiths.oculichatinterface.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 允许cookies
        config.setAllowCredentials(true);

        // 允许的源
        config.addAllowedOrigin("http://localhost:8000");

        // 允许的头信息
        config.addAllowedHeader("*");

        // 允许的HTTP方法
        config.addAllowedMethod("*");

        // 预检请求的有效期，单位秒
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
