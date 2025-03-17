package com.caiths.oculichatback.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 电子邮件配置类。
 * <p>
 * 该类用于加载和管理电子邮件服务相关的配置，通过 {@code application.properties} 或
 * {@code application.yml} 文件中的 {@code spring.mail} 前缀进行属性绑定。
 * </p>
 *
 * <p><strong>主要功能：</strong></p>
 * <ul>
 *   <li>定义默认的发件人邮箱地址。</li>
 *   <li>支持通过配置文件快速调整邮件发送的相关参数。</li>
 * </ul>
 *
 * <p><strong>示例配置：</strong></p>
 * <pre>
 * spring.mail:
 *   emailFrom: your-email@example.com
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
@Data
public class EmailConfig {

    /**
     * 默认发件人邮箱地址。
     * <p>
     * 此属性用于指定发送邮件时的默认发件人邮箱。如果未在配置文件中指定，默认值为 {@code 83876519@qq.com}。
     * </p>
     *
     * <p><strong>配置示例：</strong></p>
     * <pre>
     * spring.mail.emailFrom: your-email@example.com
     * </pre>
     */
    private String emailFrom = "83876519@qq.com";
}
