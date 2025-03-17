package com.caiths.oculichatback.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里支付账户配置类，用于加载和管理阿里支付相关的配置信息。
 * <p>
 * 该配置类通过 {@code application.properties} 或 {@code application.yml} 文件中的 {@code alipay} 前缀
 * 进行属性绑定。包含了与阿里支付集成所需的各项配置参数，如应用 ID、密钥、通知地址等。
 * </p>
 *
 * <p><strong>示例配置：</strong></p>
 *
 * <pre>
 * alipay:
 *   appId: your-app-id
 *   privateKey: your-private-key
 *   aliPayPublicKey: your-alipay-public-key
 *   notifyUrl: https://yourdomain.com/alipay/notify
 *   returnUrl: https://yourdomain.com/alipay/return
 *   sandbox: true
 *   sellerId: your-seller-id
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
@ConfigurationProperties(prefix = "alipay")
@Component
public class AliPayAccountConfig {

    /**
     * 应用 ID，由阿里支付分配，用于标识具体的应用。
     */
    private String appId;

    /**
     * 商户私钥，用于签名请求和验证响应的安全密钥。
     * <p>
     * 请妥善保管此私钥，避免泄露，以确保交易的安全性。
     * </p>
     */
    private String privateKey;

    /**
     * 支付宝公钥，用于验证支付宝返回的签名，确保响应的真实性。
     */
    private String aliPayPublicKey;

    /**
     * 支付宝异步通知 URL，用于接收支付宝服务器发送的支付结果通知。
     * <p>
     * 该 URL 必须能够被支付宝服务器访问，并且需要确保安全性。
     * </p>
     */
    private String notifyUrl;

    /**
     * 支付宝同步返回的 URL，用于在支付完成后跳转回商户网站。
     */
    private String returnUrl;

    /**
     * 是否使用沙箱环境。
     * <p>
     * {@code true} 表示使用沙箱环境进行测试，{@code false} 表示使用正式环境。
     * </p>
     */
    private Boolean sandbox;

    /**
     * 卖家 ID，通常为商户的支付宝用户 ID。
     * <p>
     * 用于标识具体的卖家账户，确保资金能够正确到账。
     * </p>
     */
    private String sellerId;
}
