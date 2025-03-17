package com.caiths.oculichatback;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * OculichatBackendApplication 是后端应用程序的主启动类。
 * <p>
 * 它整合了 Spring Boot、MyBatis 和 Apache Dubbo 等框架，
 * 并通过 @EnableScheduling 和 @EnableDubbo 实现了定时任务与分布式服务功能。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@SpringBootApplication
@EnableScheduling
@EnableDubbo
@MapperScan("com.caiths.oculichatback.mapper")
public class OculichatBackApplication {

    /**
     * 应用程序的主入口方法。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(OculichatBackApplication.class, args);
    }
}
