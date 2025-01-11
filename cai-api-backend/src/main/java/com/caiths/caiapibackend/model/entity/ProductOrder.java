package com.caiths.caiapibackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品订单实体类。
 * <p>
 * 该类映射到数据库中的 `product_order` 表，用于记录商品订单的详细信息，包括订单号、支付方式、商品信息等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@TableName(value = "product_order")
@Data
public class ProductOrder implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识符，自增长。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 微信订单号或支付宝订单ID，用于唯一标识订单。
     */
    private String orderNo;

    /**
     * 支付方式，用于标识订单的支付方式。
     * <p>示例类型包括：
     * <ul>
     *     <li>WX - 微信支付</li>
     *     <li>ZFB - 支付宝支付</li>
     * </ul>
     * </p>
     */
    private String payType;

    /**
     * 过期时间，表示订单的有效期。
     */
    private Date expirationTime;

    /**
     * 商品名称，用于标识和描述商品。
     */
    private String orderName;

    /**
     * 二维码地址，用于展示支付二维码。
     */
    private String codeUrl;

    /**
     * 商品信息，详细描述订单中包含的商品。
     */
    private String productInfo;

    /**
     * 金额（单位：分），表示订单的总金额。
     */
    private Integer total;

    /**
     * 商品ID，标识订单中包含的具体商品。
     */
    private Long productId;

    /**
     * 创建人ID，标明创建该订单的用户。
     */
    private Long userId;

    /**
     * 订单状态，用于表示当前订单的状态。
     * <p>示例状态包括：
     * <ul>
     *     <li>SUCCESS - 支付成功</li>
     *     <li>REFUND - 转入退款</li>
     *     <li>NOTPAY - 未支付</li>
     *     <li>CLOSED - 已关闭</li>
     *     <li>REVOKED - 已撤销（仅付款码支付会返回）</li>
     *     <li>USERPAYING - 用户支付中（仅付款码支付会返回）</li>
     *     <li>PAYERROR - 支付失败（仅付款码支付会返回）</li>
     * </ul>
     * </p>
     */
    private String status;

    /**
     * 创建时间，记录订单的创建时间。
     */
    private Date createTime;

    /**
     * 更新时间，记录订单的最后更新时间。
     */
    private Date updateTime;

    /**
     * 增加的积分个数，用于激励用户持续购买或使用服务。
     */
    private Integer addPoints;

    /**
     * 支付宝订单体，存储支付宝支付时的表单数据。
     */
    private String formData;
}
