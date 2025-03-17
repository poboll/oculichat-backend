package com.caiths.oculichatback.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品类型状态枚举。
 * <p>
 * 定义了不同的产品类型及其对应的描述和标识值。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public enum ProductTypeStatusEnum {

    /**
     * VIP会员。
     */
    VIP("VIP会员", "VIP"),

    /**
     * 余额充值。
     */
    RECHARGE("余额充值", "RECHARGE"),

    /**
     * 充值活动。
     */
    RECHARGE_ACTIVITY("充值活动", "RECHARGEACTIVITY");

    /**
     * 产品类型描述。
     */
    private final String text;

    /**
     * 产品类型值。
     */
    private final String value;

    /**
     * 构造方法。
     *
     * @param text  产品类型描述
     * @param value 产品类型值
     */
    ProductTypeStatusEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有产品类型值的列表。
     *
     * @return 产品类型值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(ProductTypeStatusEnum::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 获取产品类型值。
     *
     * @return 产品类型值
     */
    public String getValue() {
        return value;
    }

    /**
     * 获取产品类型描述。
     *
     * @return 产品类型描述
     */
    public String getText() {
        return text;
    }
}
