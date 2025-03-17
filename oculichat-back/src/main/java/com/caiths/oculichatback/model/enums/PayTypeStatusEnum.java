package com.caiths.oculichatback.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 支付类型状态枚举。
 * <p>
 * 定义了不同的支付类型及其对应的描述和标识值。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public enum PayTypeStatusEnum {

    /**
     * 微信支付。
     */
    WX("微信支付", "WX"),

    /**
     * 支付宝支付。
     */
    ALIPAY("支付宝支付", "ALIPAY");

    /**
     * 支付类型描述。
     */
    private final String text;

    /**
     * 支付类型值。
     */
    private final String value;

    /**
     * 构造方法。
     *
     * @param text  支付类型描述
     * @param value 支付类型值
     */
    PayTypeStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有支付类型值的列表。
     *
     * @return 支付类型值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(PayTypeStatusEnum::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 获取支付类型值。
     *
     * @return 支付类型值
     */
    public String getValue() {
        return value;
    }

    /**
     * 获取支付类型描述。
     *
     * @return 支付类型描述
     */
    public String getText() {
        return text;
    }
}
