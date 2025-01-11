package com.caiths.caiapicommon.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户接口调用表，记录用户对接口的调用信息。
 *
 * <p>包括调用次数、调用状态以及关联的用户和接口信息。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@TableName(value = "user_interface_invoke")
@Data
public class UserInterfaceInvoke implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 调用记录的唯一标识。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 调用人的用户 ID。
     */
    private Long userId;

    /**
     * 被调用的接口 ID。
     */
    private Long interfaceId;

    /**
     * 接口的总调用次数。
     */
    private Long totalInvokes;

    /**
     * 调用状态（0- 正常，1- 封号）。
     */
    private Integer status;

    /**
     * 调用记录的创建时间。
     */
    private Date createTime;

    /**
     * 调用记录的最后更新时间。
     */
    private Date updateTime;

    /**
     * 标识调用记录是否被删除（1- 删除，0- 未删除）。
     */
    @TableLogic
    private Integer isDelete;
}
