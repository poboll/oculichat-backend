package com.caiths.oculichatback.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局跨域配置类。
 *
 * <p>该类实现了 {@link WebMvcConfigurer} 接口，用于配置全局的跨域资源共享 (CORS) 策略。</p>
 * <p>跨域配置允许前端应用与后端服务之间进行跨域请求，特别适用于前后端分离的项目架构。</p>
 *
 * <p><strong>主要功能：</strong></p>
 * <ul>
 *   <li>允许所有路径的跨域请求</li>
 *   <li>支持 Cookie 的跨域传递</li>
 *   <li>支持多种 HTTP 方法，如 GET、POST、PUT、DELETE 等</li>
 *   <li>放行所有请求头和暴露所有响应头</li>
 * </ul>
 *
 * <p><strong>注意：</strong>使用 {@code allowedOriginPatterns("*")} 配置时，请注意安全性，
 * 在生产环境下应限制具体的域名而不是使用通配符。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 配置跨域映射规则。
     *
     * <p>覆盖所有路径的跨域请求，支持多种 HTTP 方法和所有请求头，并允许客户端发送 Cookie。</p>
     *
     * @param registry 用于注册跨域映射的 {@link CorsRegistry} 对象
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 覆盖所有请求路径
        registry.addMapping("/**")
                // 允许发送 Cookie
                .allowCredentials(true)
                // 允许的域名模式（使用 patterns 避免与 allowCredentials 冲突）
                .allowedOriginPatterns("*")
                // 允许的 HTTP 方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许的请求头
                .allowedHeaders("*")
                // 暴露的响应头
                .exposedHeaders("*");
    }
}
