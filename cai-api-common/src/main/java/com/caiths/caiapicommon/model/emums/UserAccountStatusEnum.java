package com.caiths.caiapicommon.model.emums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户帐户状态枚举，定义用户帐户的不同状态及其对应的描述和数值。
 *
 * <p>包括用户帐户的正常和封禁状态。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public enum UserAccountStatusEnum {

    /**
     * 正常状态，表示用户帐户处于正常使用中。
     */
    NORMAL("正常", 0),

    /**
     * 封禁状态，表示用户帐户已被封禁。
     */
    BAN("封禁", 1);

    /**
     * 状态描述。
     */
    private final String text;

    /**
     * 状态对应的数值。
     */
    private final int value;

    /**
     * 构造一个新的 UserAccountStatusEnum 实例。
     *
     * @param text  状态描述
     * @param value 状态对应的数值
     */
    UserAccountStatusEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有状态对应的数值列表。
     *
     * @return {@link List}<{@link Integer}> 状态数值列表
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 获取状态的数值。
     *
     * @return 状态数值
     */
    public int getValue() {
        return value;
    }

    /**
     * 获取状态的描述文本。
     *
     * @return 状态描述
     */
    public String getText() {
        return text;
    }
}
