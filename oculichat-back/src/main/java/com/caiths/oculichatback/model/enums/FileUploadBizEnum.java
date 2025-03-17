package com.caiths.oculichatback.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 文件上传业务类型枚举。
 * <p>
 * 定义了不同的文件上传业务类型及其对应的描述和标识。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public enum FileUploadBizEnum {
    /**
     * 用户头像。
     */
    USER_AVATAR("用户头像", "user_avatar"),

    /**
     * 接口头像。
     */
    INTERFACE_AVATAR("接口头像", "interface_avatar");

    /**
     * 类型描述。
     */
    private final String text;

    /**
     * 类型值。
     */
    private final String value;

    /**
     * 构造方法。
     *
     * @param text  类型描述
     * @param value 类型值
     */
    FileUploadBizEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取所有类型值的列表。
     *
     * @return 类型值列表
     */
    public static List<String> getValues() {
        return Arrays.stream(values())
                .map(FileUploadBizEnum::getValue)
                .collect(Collectors.toList());
    }

    /**
     * 根据类型值获取对应的枚举。
     *
     * @param value 类型值
     * @return 对应的 {@link FileUploadBizEnum} 枚举，如果未找到则返回 {@code null}
     */
    public static FileUploadBizEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (FileUploadBizEnum anEnum : FileUploadBizEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 获取类型值。
     *
     * @return 类型值
     */
    public String getValue() {
        return value;
    }

    /**
     * 获取类型描述。
     *
     * @return 类型描述
     */
    public String getText() {
        return text;
    }
}
