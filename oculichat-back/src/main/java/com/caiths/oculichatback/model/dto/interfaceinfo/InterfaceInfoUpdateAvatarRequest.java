package com.caiths.oculichatback.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 接口信息更新头像请求数据传输对象。
 * <p>
 * 该类用于封装更新接口头像的请求参数，包括接口的唯一标识符和头像 URL。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class InterfaceInfoUpdateAvatarRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 接口的唯一标识符。
     */
    private long id;

    /**
     * 接口头像的 URL 地址。
     */
    private String avatarUrl;
}
