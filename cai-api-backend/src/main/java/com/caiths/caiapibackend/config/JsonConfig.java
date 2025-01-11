package com.caiths.caiapibackend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * JSON 配置类，用于自定义应用程序的 JSON 序列化行为。
 * <p>
 * 该配置主要解决 Java 中 `Long` 类型在序列化为 JSON 时，因前端 JavaScript 精度问题导致的数据丢失情况。
 * 通过将 `Long` 类型序列化为字符串，确保数据的完整性。
 * </p>
 *
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>自定义序列化规则，将 {@code Long} 和 {@code long} 类型序列化为字符串。</li>
 *   <li>通过 Jackson 的 {@link ObjectMapper} 注册自定义模块实现。</li>
 * </ul>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@JsonComponent
public class JsonConfig {

    /**
     * 定制化的 {@link ObjectMapper} Bean。
     * <p>
     * 该方法为 Jackson 提供一个自定义的 {@link ObjectMapper}，并配置了一个 {@link SimpleModule} 模块，
     * 用于将所有 {@code Long} 和 {@code long} 类型字段序列化为字符串，从而避免前端 JavaScript
     * 处理大数值时的精度丢失问题。
     * </p>
     *
     * <p><strong>注意：</strong> 如果应用中有其他场景需要特殊的序列化或反序列化规则，可以在此处扩展。</p>
     *
     * @param builder Jackson 的 {@link Jackson2ObjectMapperBuilder}，用于构建 {@link ObjectMapper}
     * @return 配置后的 {@link ObjectMapper} 实例
     */
    @Bean
    public ObjectMapper jacksonObjectMapper(Jackson2ObjectMapperBuilder builder) {
        // 使用 Jackson Builder 创建 ObjectMapper 实例
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        // 创建一个简单模块，定义自定义序列化规则
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance); // Long 类型序列化为字符串
        module.addSerializer(Long.TYPE, ToStringSerializer.instance); // long 类型序列化为字符串

        // 将模块注册到 ObjectMapper 中
        objectMapper.registerModule(module);

        return objectMapper;
    }
}
