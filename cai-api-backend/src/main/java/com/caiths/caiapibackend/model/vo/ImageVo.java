package com.caiths.caiapibackend.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 上传图片状态的值对象，用于封装上传图片相关的信息。
 * <p>
 * 包含图片的唯一标识、名称、状态及其访问 URL 等信息。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class ImageVo implements Serializable {
    private static final long serialVersionUID = -4296258656223039373L;

    /**
     * 图片的唯一标识。
     */
    private String uid;

    /**
     * 图片的名称。
     */
    private String name;

    /**
     * 图片的上传状态。
     */
    private String status;

    /**
     * 图片的访问 URL。
     */
    private String url;
}
