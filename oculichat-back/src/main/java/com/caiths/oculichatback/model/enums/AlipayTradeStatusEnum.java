package com.caiths.oculichatback.model.enums;

import lombok.Getter;

import static com.caiths.oculichatback.model.enums.PaymentStatusEnum.*;

/**
 * 支付宝贸易状态枚举。
 * <p>
 * 定义了支付宝交易的各种状态及其对应的支付状态。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Getter
public enum AlipayTradeStatusEnum {

    /**
     * 交易创建，等待买家付款。
     */
    WAIT_BUYER_PAY(NOTPAY),

    /**
     * 交易在指定时间段内未支付时关闭；
     * 或在交易完成全额退款成功时关闭的交易。
     */
    TRADE_CLOSED(CLOSED),

    /**
     * 交易成功，且可对该交易做操作，如：多级分润、退款等。
     */
    TRADE_SUCCESS(SUCCESS),

    /**
     * 等待卖家收款（买家付款后，如果卖家账号被冻结）。
     */
    TRADE_PENDING(NOTPAY),

    /**
     * 交易成功且结束，即不可再做任何操作。
     */
    TRADE_FINISHED(SUCCESS);

    /**
     * 对应的支付状态枚举。
     */
    private final PaymentStatusEnum paymentStatusEnum;

    /**
     * 构造方法。
     *
     * @param paymentStatusEnum 对应的支付状态枚举
     */
    AlipayTradeStatusEnum(PaymentStatusEnum paymentStatusEnum) {
        this.paymentStatusEnum = paymentStatusEnum;
    }

    /**
     * 按名称查找对应的 {@link AlipayTradeStatusEnum} 枚举。
     *
     * @param name 枚举名称
     * @return 对应的 {@link AlipayTradeStatusEnum} 枚举
     * @throws RuntimeException 如果未找到对应的枚举
     */
    public static AlipayTradeStatusEnum findByName(String name) {
        for (AlipayTradeStatusEnum statusEnum : AlipayTradeStatusEnum.values()) {
            if (name.equalsIgnoreCase(statusEnum.name())) {
                return statusEnum;
            }
        }
        throw new RuntimeException("错误的支付宝支付状态");
    }
}
