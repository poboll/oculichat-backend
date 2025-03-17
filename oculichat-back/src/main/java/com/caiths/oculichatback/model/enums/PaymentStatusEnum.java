package com.caiths.oculichatback.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 付款状态枚举。
 * <p>
 * 定义了付款过程中的各种状态及其对应的描述和标识值。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public enum PaymentStatusEnum {

    /**
     * 支付成功。
     */
    SUCCESS("支付成功", "SUCCESS"),

    /**
     * 支付失败。
     */
    PAY_ERROR("支付失败", "PAYERROR"),

    /**
     * 用户支付中。
     */
    USER_PAYING("用户支付中", "USER_PAYING"),

    /**
     * 已关闭。
     */
    CLOSED("已关闭", "CLOSED"),

    /**
     * 未支付。
     */
    NOTPAY("未支付", "NOTPAY"),

    /**
     * 转入退款。
     */
    REFUND("转入退款", "REFUND"),

    /**
     * 退款中。
     */
    PROCESSING("退款中", "PROCESSING"),

    /**
     * 已撤销（刷卡支付）。
     */
    REVOKED("已撤销（刷卡支付）", "REVOKED"),

    /**
     * 未知状态。
     */
    UNKNOW("未知状态", "UNKNOW");

    /**
     * 状态描述。
     */
    private final String text;

    /**
     * 状态值。
     */
    private final String value;

    /**
     * 构造方法。
     *
     * @param text  状态描述
     * @param value 状态值
     */
    PaymentStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有状态值的列表。
     *
     * @return 状态值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(PaymentStatusEnum::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 获取状态值。
     *
     * @return 状态值
     */
    public String getValue() {
        return value;
    }

    /**
     * 获取状态描述。
     *
     * @return 状态描述
     */
    public String getText() {
        return text;
    }
}
