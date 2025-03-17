package com.caiths.oculichatback.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品信息状态枚举。
 * <p>
 * 定义了产品信息的不同状态及其对应的描述和标识值。
 * </p>
 *
 * @author yupi
 * @version 1.0
 * @since 2024-12-02
 */
public enum ProductInfoStatusEnum {

    /**
     * 发布状态。
     */
    ONLINE("开启", 1),

    /**
     * 下线状态。
     */
    OFFLINE("关闭", 2),

    /**
     * 审计中状态。
     */
    AUDITING("审核中", 0);

    /**
     * 状态描述。
     */
    private final String text;

    /**
     * 状态值。
     */
    private final int value;

    /**
     * 构造方法。
     *
     * @param text  状态描述
     * @param value 状态值
     */
    ProductInfoStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有状态值的列表。
     *
     * @return 状态值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values())
                .map(ProductInfoStatusEnum::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 获取状态值。
     *
     * @return 状态值
     */
    public int getValue() {
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
