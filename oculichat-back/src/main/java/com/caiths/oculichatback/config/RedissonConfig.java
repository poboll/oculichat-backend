package com.caiths.oculichatback.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置类，用于配置和初始化 Redisson 客户端以连接 Redis。
 * <p>
 * 该配置类通过 {@code spring.redis} 前缀从配置文件（{@code application.properties} 或 {@code application.yml}）
 * 加载 Redis 相关的配置信息，并创建 {@link RedissonClient} Bean，以供应用程序中使用。
 * </p>
 *
 * <p><strong>主要功能：</strong></p>
 * <ul>
 *   <li>加载 Redis 服务器的主机和端口信息。</li>
 *   <li>配置 Redisson 客户端连接的详细参数，如数据库编号。</li>
 *   <li>创建并初始化 {@link RedissonClient} 实例，供应用程序中使用。</li>
 * </ul>
 *
 * <p><strong>示例配置：</strong></p>
 * <pre>
 * spring.redis:
 *   host: localhost
 *   port: 6379
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    /**
     * Redis 服务器的主机地址。
     * <p>
     * 例如：{@code localhost}
     * </p>
     */
    private String host;

    /**
     * Redis 服务器的端口号。
     * <p>
     * 例如：{@code 6379}
     * </p>
     */
    private String port;

    /**
     * Redis 数据库编号。
     * <p>
     * 默认使用数据库编号 {@code 3}，可以根据需要在配置文件中进行调整。
     * </p>
     *
     * <p><strong>示例：</strong></p>
     * <pre>
     * spring.redis.database: 3
     * </pre>
     */
    private int database = 3;

    /**
     * 创建并配置 {@link RedissonClient} Bean，用于与 Redis 服务器通信。
     * <p>
     * 该方法根据注入的 {@code host} 和 {@code port} 配置 Redisson 客户端的连接地址，
     * 并设置连接的数据库编号。通过 {@link Redisson#create(Config)} 方法实例化 Redisson 客户端。
     * </p>
     *
     * <p><strong>功能描述：</strong></p>
     * <ul>
     *   <li>初始化 Redisson 客户端的配置。</li>
     *   <li>设置 Redis 服务器的连接地址和数据库编号。</li>
     *   <li>创建并返回 Redisson 客户端实例。</li>
     * </ul>
     *
     * @return 配置完成的 {@link RedissonClient} 实例
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        // 创建配置
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer()
                .setAddress(redisAddress)
                .setDatabase(this.database);

        // 创建并返回 Redisson 客户端实例
        return Redisson.create(config);
    }
}
