package com.caiths.oculichatback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Knife4j 配置类，用于集成和配置 Swagger 文档生成工具。
 * <p>
 * 该配置类通过 {@link springfox.documentation.spring.web.plugins.Docket} Bean
 * 定义了 API 文档的基本信息和扫描路径。仅在非生产环境（{@code !prod}）下启用，
 * 以避免在生产环境中暴露 API 文档。
 * </p>
 *
 * <p><strong>主要功能：</strong></p>
 * <ul>
 *   <li>配置 Swagger 文档的基本信息，如标题、描述和版本。</li>
 *   <li>指定扫描的 Controller 包路径，自动生成对应的 API 文档。</li>
 *   <li>根据当前的 Spring Profile 环境决定是否启用 Swagger。</li>
 * </ul>
 *
 * <p><strong>注意：</strong> 该配置仅在非生产环境下生效（{@code !prod}），
 * 确保生产环境的安全性和性能。</p>
 *
 * <p><strong>示例配置：</strong></p>
 * <pre>
 * spring.profiles.active=dev
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Configuration
@EnableSwagger2
@Profile("!prod")
public class Knife4jConfig {

    /**
     * 创建并配置 {@link Docket} Bean，用于生成 Swagger API 文档。
     * <p>
     * 该方法配置了 API 文档的标题、描述和版本，并指定了需要扫描的 Controller 包路径。
     * 通过 {@code DocumentationType.OAS_30} 指定使用 OpenAPI 3.0 规范。
     * </p>
     *
     * <p><strong>功能描述：</strong></p>
     * <ul>
     *   <li>设置 API 文档的基本信息，如标题、描述和版本。</li>
     *   <li>指定需要生成文档的 Controller 包路径。</li>
     *   <li>包含所有路径的 API 接口（{@code PathSelectors.any()}）。</li>
     * </ul>
     *
     * @return 配置完成的 {@link Docket} 实例
     */
    @Bean
    public Docket defaultApi2() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(new ApiInfoBuilder()
                        .title("Project Backend API")
                        .description("Project Backend API Documentation")
                        .version("1.0")
                        .build())
                .select()
                // 指定 Controller 扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.caiths.oculichatback.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
