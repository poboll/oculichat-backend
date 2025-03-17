package com.caiths.oculichatback.model.alipay;

import lombok.Data;

import java.io.Serializable;

/**
 * AliPay 异步响应数据模型。
 * <p>
 * 该类用于封装支付宝异步通知的响应数据，包含了交易的各个相关信息。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class AliPayAsyncResponse implements Serializable {

    private static final long serialVersionUID = 1061553753198699097L;

    /**
     * 通知时间。
     */
    private String notifyTime;

    /**
     * 通知的类型。
     */
    private String notifyType;

    /**
     * 通知校验 ID。
     */
    private String notifyId;

    /**
     * 卖家 ID。
     */
    private String sellerId;

    /**
     * 买方 ID。
     */
    private String buyerId;

    /**
     * 编码格式。
     */
    private String charset;

    /**
     * 接口版本。
     */
    private String version;

    /**
     * 授权方的 App ID。
     */
    private String authAppId;

    /**
     * 支付宝交易号。
     */
    private String tradeNo;

    /**
     * 应用 ID。
     */
    private String appId;

    /**
     * 商户订单号。
     */
    private String outTradeNo;

    /**
     * 交易状态。
     */
    private String tradeStatus;

    /**
     * 订单金额。
     */
    private String totalAmount;

    /**
     * 实收金额。
     */
    private String receiptAmount;

    /**
     * 付款金额。
     */
    private String buyerPayAmount;

    /**
     * 订单标题。
     */
    private String subject;

    /**
     * 商品描述。
     */
    private String body;

    /**
     * 交易创建时间。
     */
    private String gmtCreate;
}
