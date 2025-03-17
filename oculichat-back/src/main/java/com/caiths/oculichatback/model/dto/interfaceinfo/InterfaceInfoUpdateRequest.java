package com.caiths.oculichatback.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接口信息更新请求数据传输对象。
 * <p>
 * 用于封装更新接口信息的请求参数，包括接口的基本信息、请求参数、响应参数等内容。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class InterfaceInfoUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 接口唯一标识 ID。
     */
    private long id;

    /**
     * 接口名称。
     */
    private String name;

    /**
     * 返回数据的格式。
     */
    private String returnFormat;

    /**
     * 接口响应参数列表。
     */
    private List<ResponseParamsField> responseParams;

    /**
     * 接口访问地址。
     */
    private String url;

    /**
     * 请求方法类型（如 GET、POST）。
     */
    private String method;

    /**
     * 调用接口时所需扣减的积分数。
     */
    private Integer reduceScore;

    /**
     * 接口头像的 URL 地址。
     */
    private String avatarUrl;

    /**
     * 描述信息，提供对接口的简要说明。
     */
    private String description;

    /**
     * 请求示例，示范接口调用的格式和内容。
     */
    private String requestExample;

    /**
     * 请求头参数，包含调用接口时需要附加的头信息。
     */
    private String requestHeader;

    /**
     * 响应头参数，描述接口响应中返回的头信息。
     */
    private String responseHeader;

    /**
     * 接口请求参数列表。
     */
    private List<RequestParamsField> requestParams;

    /**
     * 接口状态（0 - 默认下线，1 - 上线）。
     */
    private Integer status;
}
