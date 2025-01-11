package com.caiths.caiapibackend.model.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户帐户状态枚举。
 * <p>
 * 定义了用户帐户的不同状态及其对应的描述和标识值。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public enum UserAccountStatusEnum {

    /**
     * 正常状态。
     */
    NORMAL("正常", 0),

    /**
     * 封禁状态。
     */
    BAN("封禁", 1);

    /**
     * 用户帐户状态描述。
     */
    private final String text;

    /**
     * 用户帐户状态值。
     */
    private final int value;

    /**
     * 构造方法。
     *
     * @param text  用户帐户状态描述
     * @param value 用户帐户状态值
     */
    UserAccountStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有用户帐户状态值的列表。
     *
     * @return 用户帐户状态值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values())
                .map(UserAccountStatusEnum::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 获取用户帐户状态值。
     *
     * @return 用户帐户状态值
     */
    public int getValue() {
        return value;
    }

    /**
     * 获取用户帐户状态描述。
     *
     * @return 用户帐户状态描述
     */
    public String getText() {
        return text;
    }
}
