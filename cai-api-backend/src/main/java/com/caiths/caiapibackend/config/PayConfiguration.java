package com.caiths.caiapibackend.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.ijpay.alipay.AliPayApiConfig;
import com.ijpay.alipay.AliPayApiConfigKit;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 支付配置类，集成了微信支付和支付宝支付的相关配置。
 * <p>
 * 该类通过注入 {@link WxPayAccountConfig} 和 {@link AliPayAccountConfig}，分别为微信支付和支付宝支付
 * 提供必要的账户信息，并创建对应的支付服务实例。
 * </p>
 *
 * <p><strong>主要功能：</strong></p>
 * <ul>
 *   <li>配置微信支付服务（{@link WxPayService}）。</li>
 *   <li>配置支付宝支付服务（使用 {@link AliPayApiConfigKit} 设置全局支付宝 API 配置）。</li>
 *   <li>支持沙箱环境的切换，便于调试支付接口。</li>
 * </ul>
 *
 * <p><strong>示例配置：</strong></p>
 * <pre>
 * # 微信支付配置
 * wxpay:
 *   app-id: your-wxpay-app-id
 *   mch-id: your-wxpay-mch-id
 *   api-v3-key: your-wxpay-api-v3-key
 *   private-key-path: your-private-key-path
 *   private-cert-path: your-private-cert-path
 *   notify-url: your-notify-url
 *   sandbox: false
 *
 * # 支付宝配置
 * alipay:
 *   app-id: your-alipay-app-id
 *   ali-pay-public-key: your-alipay-public-key
 *   private-key: your-alipay-private-key
 *   sandbox: false
 * </pre>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Configuration
@AllArgsConstructor
public class PayConfiguration {

    @Resource
    private WxPayAccountConfig properties;

    @Resource
    private AliPayAccountConfig aliPayAccountConfig;

    /**
     * 配置微信支付服务。
     * <p>
     * 该方法根据注入的 {@link WxPayAccountConfig} 配置微信支付的账户信息，包括 AppID、商户号、
     * API v3 密钥、私钥路径、证书路径等。此外，还支持配置是否使用沙箱环境以便于调试。
     * </p>
     *
     * <p><strong>功能描述：</strong></p>
     * <ul>
     *   <li>初始化微信支付服务实例。</li>
     *   <li>设置支付通知 URL 和沙箱环境。</li>
     * </ul>
     *
     * @return 配置完成的 {@link WxPayService} 实例
     */
    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(StringUtils.trimToNull(this.properties.getAppId()));
        payConfig.setMchId(StringUtils.trimToNull(this.properties.getMchId()));
        payConfig.setApiV3Key(StringUtils.trimToNull(this.properties.getApiV3Key()));
        payConfig.setPrivateKeyPath(StringUtils.trimToNull(this.properties.getPrivateKeyPath()));
        payConfig.setPrivateCertPath(StringUtils.trimToNull(this.properties.getPrivateCertPath()));
        payConfig.setNotifyUrl(StringUtils.trimToNull(this.properties.getNotifyUrl()));
        payConfig.setUseSandboxEnv(this.properties.isSandbox());

        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;
    }

    /**
     * 配置支付宝支付服务。
     * <p>
     * 该方法根据注入的 {@link AliPayAccountConfig} 配置支付宝支付的账户信息，包括 AppID、公钥、私钥、
     * 接口地址（支持沙箱和正式环境）、签名方式等。配置完成后，通过 {@link AliPayApiConfigKit} 设置为线程本地变量，
     * 供后续支付操作使用。
     * </p>
     *
     * <p><strong>功能描述：</strong></p>
     * <ul>
     *   <li>初始化支付宝支付 API 配置。</li>
     *   <li>支持切换沙箱环境和正式环境。</li>
     * </ul>
     */
    @Bean
    public void aliPayApi() {
        AliPayApiConfig aliPayApiConfig = AliPayApiConfig.builder()
                .setAppId(aliPayAccountConfig.getAppId())
                .setAliPayPublicKey(aliPayAccountConfig.getAliPayPublicKey())
                .setCharset("UTF-8")
                .setPrivateKey(aliPayAccountConfig.getPrivateKey())
                .setServiceUrl(aliPayAccountConfig.getSandbox()
                        ? "https://openapi-sandbox.dl.alipaydev.com/gateway.do"
                        : "https://openapi.alipay.com/gateway.do")
                .setSignType("RSA2")
                .setCertModel(false)
                .build();
        AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
    }
}
