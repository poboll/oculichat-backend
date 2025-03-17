package com.caiths.oculichatback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * RedisTemplate 配置类，用于配置和初始化 {@link RedisTemplate} 以与 Redis 进行交互。
 * <p>
 * 该配置类通过定义 {@code RedisTemplate} Bean，设置了键和值的序列化方式，确保
 * 数据在存储和读取时的正确性与高效性。默认配置使用 {@link StringRedisSerializer} 作为
 * 键的序列化器，以及 {@link GenericJackson2JsonRedisSerializer} 作为值的序列化器。
 * </p>
 *
 * <p><strong>主要功能：</strong></p>
 * <ul>
 *   <li>配置 RedisTemplate 的连接工厂，确保其与 Redis 服务器的正确连接。</li>
 *   <li>设置键和值的序列化方式，以支持不同类型的数据存储与检索。</li>
 *   <li>提供一个通用的 RedisTemplate Bean，供应用程序中各个模块使用。</li>
 * </ul>
 *
 * <p><strong>示例配置：</strong></p>
 * <pre>
 * spring.redis:
 *   host: localhost
 *   port: 6379
 *   password: your-redis-password
 *   database: 0
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Configuration
public class RedisTemplateConfig {

    /**
     * 创建并配置 {@link RedisTemplate} Bean，用于与 Redis 进行数据交互。
     * <p>
     * 该方法设置了键和值的序列化方式，以确保数据在存储和读取时的正确性。
     * 默认情况下，键使用 {@link StringRedisSerializer} 进行序列化，值使用
     * {@link GenericJackson2JsonRedisSerializer} 进行序列化。这种配置方式支持
     * 存储复杂的对象类型，并且在数据传输过程中保持数据的可读性。
     * </p>
     *
     * <p><strong>功能描述：</strong></p>
     * <ul>
     *   <li>初始化 {@link RedisTemplate} 实例并设置其连接工厂。</li>
     *   <li>配置键的序列化方式为 {@link StringRedisSerializer}，适用于字符串类型的键。</li>
     *   <li>配置值的序列化方式为 {@link GenericJackson2JsonRedisSerializer}，适用于 JSON 格式的数据。</li>
     *   <li>确保 RedisTemplate 在应用程序中被广泛使用时具有一致的序列化行为。</li>
     * </ul>
     *
     * <p><strong>示例使用：</strong></p>
     * <pre>
     * @Autowired
     * private RedisTemplate&lt;String, Object&gt; redisTemplate;
     *
     * public void saveData(String key, Object value) {
     *     redisTemplate.opsForValue().set(key, value);
     * }
     *
     * public Object getData(String key) {
     *     return redisTemplate.opsForValue().get(key);
     * }
     * </pre>
     *
     * @param connectionFactory Redis 连接工厂，用于创建与 Redis 服务器的连接
     * @return 配置完成的 {@link RedisTemplate}&lt;{@link String}, {@link Object}&gt; 实例
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // 创建 RedisTemplate 实例
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂
        redisTemplate.setConnectionFactory(connectionFactory);

        // 设置键的序列化器为 StringRedisSerializer
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);

        // 设置值的序列化器为 GenericJackson2JsonRedisSerializer
        RedisSerializer<Object> jsonSerializer = new GenericJackson2JsonRedisSerializer();
        redisTemplate.setValueSerializer(jsonSerializer);
        redisTemplate.setHashValueSerializer(jsonSerializer);

        // 初始化 RedisTemplate 的其他属性
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
