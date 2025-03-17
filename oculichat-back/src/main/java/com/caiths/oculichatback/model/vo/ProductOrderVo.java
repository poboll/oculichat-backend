package com.caiths.oculichatback.model.vo;

import com.caiths.oculichatback.model.entity.ProductInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品订单的值对象，用于封装订单相关的详细信息。
 * <p>
 * 包含订单编号、支付类型、过期时间、商品名称、支付二维码地址等信息。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class ProductOrderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单的唯一标识 ID。
     */
    private Long id;

    /**
     * 订单编号。
     */
    private String orderNo;

    /**
     * 支付类型。
     */
    private String payType;

    /**
     * 订单过期时间。
     */
    private Date expirationTime;

    /**
     * 商品名称。
     */
    private String orderName;

    /**
     * 支付二维码的地址。
     */
    private String codeUrl;

    /**
     * 商品 ID。
     */
    private Long productId;

    /**
     * 订单金额（单位：分）。
     */
    private String total;

    /**
     * 接口订单状态。
     * <p>
     * 可能的状态包括：
     * <ul>
     *     <li>SUCCESS：支付成功</li>
     *     <li>REFUND：转入退款</li>
     *     <li>NOTPAY：未支付</li>
     *     <li>CLOSED：已关闭</li>
     *     <li>REVOKED：已撤销（仅付款码支付会返回）</li>
     *     <li>USERPAYING：用户支付中（仅付款码支付会返回）</li>
     *     <li>PAYERROR：支付失败（仅付款码支付会返回）</li>
     * </ul>
     * </p>
     */
    private String status;

    /**
     * 产品信息。
     */
    private ProductInfo productInfo;

    /**
     * 增加的积分数量。
     */
    private Integer addPoints;

    /**
     * 支付宝订单体。
     */
    private String formData;

    /**
     * 订单创建时间。
     */
    private Date createTime;

    /**
     * 产品描述。
     */
    private String description;

    /**
     * 产品类型。
     * <p>
     * 可能的类型包括：
     * <ul>
     *     <li>VIP - 会员</li>
     *     <li>RECHARGE - 充值</li>
     * </ul>
     * </p>
     */
    private String productType;
}
