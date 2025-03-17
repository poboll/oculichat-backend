package com.caiths.oculichatback.model.enums;

/**
 * 图片状态枚举。
 * <p>
 * 定义了图片处理的状态及其对应的信息。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public enum ImageStatusEnum {
    /**
     * 成功。
     */
    SUCCESS("success", "done"),

    /**
     * 参数错误。
     */
    ERROR("error", "error");

    /**
     * 状态。
     */
    private final String status;

    /**
     * 信息。
     */
    private final String value;

    /**
     * 构造方法。
     *
     * @param status 状态
     * @param value  信息
     */
    ImageStatusEnum(String status, String value) {
        this.status = status;
        this.value = value;
    }

    /**
     * 获取状态。
     *
     * @return 状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 获取信息。
     *
     * @return 信息
     */
    public String getValue() {
        return value;
    }
}
