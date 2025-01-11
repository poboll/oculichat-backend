package com.caiths.caiapibackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * ID 请求类，用于封装请求中的 ID 信息。
 *
 * <p>此类包含一个用于存储 ID 的字段。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class IdRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识符。
     */
    private Long id;
}
