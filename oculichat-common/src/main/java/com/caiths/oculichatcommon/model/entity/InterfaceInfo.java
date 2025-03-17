package com.caiths.oculichatcommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 接口信息，表示系统中各个接口的详细信息。
 *
 * <p>包括接口的基本属性，如名称、地址、请求方法等，以及统计信息和状态。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@TableName(value = "interface_info")
@Data
public class InterfaceInfo implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 接口的唯一标识。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 接口返回的数据格式，如 JSON、XML 等。
     */
    private String returnFormat;

    /**
     * 接口的名称。
     */
    private String name;

    /**
     * 接口的访问地址 URL。
     */
    private String url;

    /**
     * 发布接口的用户 ID。
     */
    private Long userId;

    /**
     * 接口的请求方法，如 GET、POST 等。
     */
    private String method;

    /**
     * 接口的总调用次数。
     */
    private Long totalInvokes;

    /**
     * 接口的请求参数信息，通常为 JSON 字符串。
     */
    private String requestParams;

    /**
     * 接口的响应参数信息，通常为 JSON 字符串。
     */
    private String responseParams;

    /**
     * 接口的描述信息，说明接口的用途和功能。
     */
    private String description;

    /**
     * 接口的请求示例，展示如何调用该接口。
     */
    private String requestExample;

    /**
     * 调用该接口需要减少的积分数量。
     */
    private Integer reduceScore;

    /**
     * 接口的头像 URL，用于展示接口的图标或标识。
     */
    private String avatarUrl;

    /**
     * 接口的请求头信息，通常为 JSON 字符串。
     */
    private String requestHeader;

    /**
     * 接口的响应头信息，通常为 JSON 字符串。
     */
    private String responseHeader;

    /**
     * 接口的状态（0- 默认下线，1- 上线）。
     */
    private Integer status;

    /**
     * 接口的创建时间。
     */
    private Date createTime;

    /**
     * 接口的最后更新时间。
     */
    private Date updateTime;

    /**
     * 标识接口是否被删除（1- 删除，0- 未删除）。
     */
    @TableLogic
    private Integer isDelete;
}
