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
 * 充值活动表实体类。
 * <p>
 * 该类映射到数据库中的 `recharge_activity` 表，用于记录充值活动的详细信息，包括订单号、用户ID、商品ID等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@TableName(value = "recharge_activity")
@Data
public class RechargeActivity implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识符，自增长。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 微信订单号或支付宝订单ID，用于唯一标识充值订单。
     */
    private String orderNo;

    /**
     * 用户ID，标明参与充值活动的用户。
     */
    private Long userId;

    /**
     * 商品ID，标识充值活动中包含的商品。
     */
    private Long productId;

    /**
     * 创建时间，记录充值活动的创建时间。
     */
    private Date createTime;

    /**
     * 更新时间，记录充值活动的最后更新时间。
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
