package com.caiths.oculichatback.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品信息实体类。
 * <p>
 * 该类映射到数据库中的 `product_info` 表，用于记录产品的详细信息，包括名称、描述、价格、类型等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@TableName(value = "product_info")
@Data
public class ProductInfo implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识符，自增长。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 产品名称，用于标识和描述产品。
     */
    private String name;

    /**
     * 状态，表示产品的当前状态。
     * <p>示例状态包括：
     * <ul>
     *     <li>0 - 默认下线</li>
     *     <li>1 - 上线</li>
     * </ul>
     * </p>
     */
    private Integer status;

    /**
     * 增加的积分个数，用于激励用户购买或使用产品。
     */
    private Integer addPoints;

    /**
     * 产品描述，提供关于产品的详细信息。
     */
    private String description;

    /**
     * 创建人ID，标明创建该产品信息的用户。
     */
    private Long userId;

    /**
     * 金额（单位：分），表示产品的价格。
     */
    private Integer total;

    /**
     * 产品类型，用于区分不同类别的产品。
     * <p>示例类型包括：
     * <ul>
     *     <li>VIP - 会员</li>
     *     <li>RECHARGE - 充值</li>
     * </ul>
     * </p>
     */
    private String productType;

    /**
     * 过期时间，表示产品的有效期。
     */
    private Date expirationTime;

    /**
     * 创建时间，记录产品信息的创建时间。
     */
    private Date createTime;

    /**
     * 更新时间，记录产品信息的最后更新时间。
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
