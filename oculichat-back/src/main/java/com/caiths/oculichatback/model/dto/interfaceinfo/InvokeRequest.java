package com.caiths.oculichatback.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 调用请求数据传输对象。
 * <p>
 * 该类用于封装调用接口时的请求参数，包括接口的唯一标识符、请求参数列表以及用户请求参数的原始字符串。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class InvokeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 接口的唯一标识符。
     */
    private Long id;

    /**
     * 请求参数列表，每个参数包含字段名称和值。
     */
    private List<Field> requestParams;

    /**
     * 用户请求参数的原始 JSON 字符串。
     */
    private String userRequestParams;

    /**
     * 请求参数字段。
     */
    @Data
    public static class Field {
        /**
         * 字段名称。
         */
        private String fieldName;

        /**
         * 字段值。
         */
        private String value;
    }
}
