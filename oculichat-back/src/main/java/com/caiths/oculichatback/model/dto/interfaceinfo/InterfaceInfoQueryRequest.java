package com.caiths.oculichatback.model.dto.interfaceinfo;

import com.caiths.oculichatback.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 接口信息查询请求数据传输对象。
 * <p>
 * 该类用于封装查询接口信息时的请求参数，包括分页信息、接口的基本信息、发布人、状态等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

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
     * 发布人 ID。
     */
    private Long userId;

    /**
     * 减少积分个数。
     */
    private Integer reduceScore;

    /**
     * 请求方法。
     */
    private String method;

    /**
     * 描述信息。
     */
    private String description;

    /**
     * 接口状态（0 - 默认下线，1 - 上线）。
     */
    private Integer status;
}
