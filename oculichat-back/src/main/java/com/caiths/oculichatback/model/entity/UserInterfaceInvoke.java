package com.caiths.oculichatback.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户接口调用表实体类。
 * <p>
 * 该类映射到数据库中的 `user_interface_invoke` 表，用于记录用户对接口的调用情况，包括调用次数、状态等。
 * </p>
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
     * 唯一标识符，自增长。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 调用人的用户ID，用于关联用户表中的用户。
     */
    private Long userId;

    /**
     * 接口ID，用于标识被调用的具体接口。
     */
    private Long interfaceId;

    /**
     * 总调用次数，记录用户对该接口的累计调用次数。
     */
    private Long totalInvokes;

    /**
     * 调用状态，表示接口调用的当前状态。
     * <p>示例状态包括：
     * <ul>
     *     <li>0 - 正常</li>
     *     <li>1 - 封号</li>
     * </ul>
     * </p>
     */
    private Integer status;

    /**
     * 创建时间，记录接口调用记录的创建时间。
     */
    private Date createTime;

    /**
     * 更新时间，记录接口调用记录的最后更新时间。
     */
    private Date updateTime;

    /**
     * 是否删除，逻辑删除标识。
     * <p>示例值包括：
     * <ul>
     *     <li>0 - 未删除</li>
     *     <li>1 - 已删除</li>
     * </ul>
     * </p>
     */
    @TableLogic
    private Integer isDelete;
}
