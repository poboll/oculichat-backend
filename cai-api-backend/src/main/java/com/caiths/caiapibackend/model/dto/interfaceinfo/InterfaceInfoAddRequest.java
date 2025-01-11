package com.caiths.caiapibackend.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接口信息创建请求数据传输对象。
 * <p>
 * 该类用于封装创建接口信息时的请求数据，包括接口的基本信息、请求参数、响应参数等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 接口名称。
     */
    private String name;

    /**
     * 返回格式。
     */
    private String returnFormat;

    /**
     * 接口地址。
     */
    private String url;

    /**
     * 接口响应参数。
     */
    private List<ResponseParamsField> responseParams;

    /**
     * 请求方法。
     */
    private String method;

    /**
     * 减少积分个数。
     */
    private Integer reduceScore;

    /**
     * 接口请求参数。
     */
    private List<RequestParamsField> requestParams;

    /**
     * 描述信息。
     */
    private String description;

    /**
     * 请求示例。
     */
    private String requestExample;

    /**
     * 请求头。
     */
    private String requestHeader;

    /**
     * 响应头。
     */
    private String responseHeader;
}
