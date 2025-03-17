package com.caiths.oculichatcommon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * OculichatCommon 应用程序的入口点。
 *
 * <p>此类用于启动 Spring Boot 应用程序，加载所有必要的配置和组件。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@SpringBootApplication
public class OculichatCommonApplication {

    /**
     * 应用程序的主方法，负责启动 Spring Boot 应用。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(OculichatCommonApplication.class, args);
    }
}
