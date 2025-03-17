package com.caiths.oculichatback.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 腾讯云对象存储客户端配置类，用于初始化和配置 {@link COSClient} 实例。
 * <p>
 * 该配置类通过 {@code application.properties} 或 {@code application.yml} 文件中的 {@code cos.client} 前缀
 * 进行属性绑定。包含了与腾讯云对象存储（COS）集成所需的各项配置参数，如访问密钥、区域、桶名等。
 * </p>
 *
 * <p><strong>示例配置：</strong></p>
 *
 * <pre>
 * cos.client:
 *   accessKey: your-access-key
 *   secretKey: your-secret-key
 *   region: ap-guangzhou
 *   bucket: your-bucket-name
 *   cosHost: https://your-cos-host.com
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Configuration
@ConfigurationProperties(prefix = "cos.client")
@Data
public class CosClientConfig {

    /**
     * 腾讯云对象存储的访问密钥 ID，用于身份验证。
     * <p>
     * 请确保此密钥的安全性，避免泄露，以防止未授权的访问。
     * </p>
     */
    private String accessKey;

    /**
     * 腾讯云对象存储的秘密访问密钥，用于身份验证。
     * <p>
     * 请妥善保管此密钥，避免泄露，以确保账户的安全性。
     * </p>
     */
    private String secretKey;

    /**
     * 腾讯云对象存储的区域标识符。
     * <p>
     * 例如：{@code ap-guangzhou} 表示广州区域。具体区域标识符请参考
     * <a href="https://www.qcloud.com/document/product/436/6224">腾讯云区域文档</a>。
     * </p>
     */
    private String region;

    /**
     * 腾讯云对象存储的存储桶名称。
     * <p>
     * 确保该存储桶已经在指定的区域中创建，并且名称唯一。
     * </p>
     */
    private String bucket;

    /**
     * 腾讯云对象存储的域名地址。
     * <p>
     * 用于访问存储在 COS 中的对象。例如：{@code https://your-cos-host.com}。
     * </p>
     */
    private String cosHost;

    /**
     * 创建并初始化一个 {@link COSClient} 实例。
     * <p>
     * 此方法会根据配置文件中的属性初始化腾讯云对象存储客户端。如果 {@code cosHost} 未配置，则抛出运行时异常。
     * </p>
     *
     * @return 初始化后的 {@link COSClient} 实例
     * @throws RuntimeException 如果 {@code cosHost} 未配置或为空
     */
    @Bean
    public COSClient cosClient() {
        if (StringUtils.isBlank(cosHost)) {
            throw new RuntimeException("cosHost未配置");
        }
        // 初始化用户身份信息 (accessKey, secretKey)
        COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
        // 设置 Bucket 的区域，COS 地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        // 生成 COS 客户端
        return new COSClient(cred, clientConfig);
    }
}
