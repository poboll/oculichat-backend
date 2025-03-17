package com.caiths.oculichatback.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付帐户配置类，用于加载和管理微信支付相关的配置信息。
 * <p>
 * 该配置类通过 {@code wx.pay} 前缀从配置文件（{@code application.properties} 或 {@code application.yml}）
 * 加载微信支付的帐户信息，包括 AppID、商户号、API v3 密钥、私钥路径、证书路径、通知地址以及是否使用沙箱环境。
 * </p>
 *
 * <p><strong>主要功能：</strong></p>
 * <ul>
 *   <li>加载微信支付所需的帐户信息配置。</li>
 *   <li>提供访问这些配置信息的 getter 和 setter 方法。</li>
 *   <li>支持通过配置文件灵活调整微信支付的运行环境（如沙箱环境）。</li>
 * </ul>
 *
 * <p><strong>示例配置：</strong></p>
 * <pre>
 * wx.pay:
 *   appId: your-wxpay-app-id
 *   mchId: your-wxpay-mch-id
 *   apiV3Key: your-wxpay-api-v3-key
 *   privateKeyPath: /path/to/your/private/key.pem
 *   privateCertPath: /path/to/your/private/cert.pem
 *   notifyUrl: https://yourdomain.com/wxpay/notify
 *   sandbox: false
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
@Component
@ConfigurationProperties(prefix = "wx.pay")
public class WxPayAccountConfig {

    /**
     * 微信公众账号或小程序的 AppID。
     * <p>
     * 例如：{@code wx1234567890abcdef}
     * </p>
     */
    private String appId;

    /**
     * 微信支付商户号。
     * <p>
     * 例如：{@code 1900000109}
     * </p>
     */
    private String mchId;

    /**
     * API v3 密钥。
     * <p>
     * 用于加密和解密微信支付 API 的请求和响应数据。
     * </p>
     */
    private String apiV3Key;

    /**
     * 私钥文件的路径。
     * <p>
     * 例如：{@code /path/to/private/key.pem}
     * </p>
     */
    private String privateKeyPath;

    /**
     * 私人证书文件的路径。
     * <p>
     * 例如：{@code /path/to/private/cert.pem}
     * </p>
     */
    private String privateCertPath;

    /**
     * 微信支付的通知地址。
     * <p>
     * 用于接收微信支付结果通知的回调 URL。
     * 例如：{@code https://yourdomain.com/wxpay/notify}
     * </p>
     */
    private String notifyUrl;

    /**
     * 是否使用微信支付的沙箱环境。
     * <p>
     * 沙箱环境用于开发和测试，模拟真实支付环境但不进行实际资金交易。
     * </p>
     *
     * <p>默认值：{@code false}</p>
     */
    private boolean sandbox = false;
}
