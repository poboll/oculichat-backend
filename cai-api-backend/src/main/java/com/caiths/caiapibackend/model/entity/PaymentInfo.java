package com.caiths.caiapibackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 付款信息实体类。
 * <p>
 * 该类映射到数据库中的 `payment_info` 表，用于记录付款相关的信息，包括订单号、交易状态、金额等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@TableName(value = "payment_info")
@Data
public class PaymentInfo implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 唯一标识符，自增长。
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 商户订单号，用于唯一标识商户端的订单。
     */
    private String orderNo;

    /**
     * 微信支付订单号，用于唯一标识微信端的订单。
     */
    private String transactionId;

    /**
     * 交易类型，指明付款的方式或类型（例如：JSAPI、NATIVE、APP）。
     */
    private String tradeType;

    /**
     * 交易状态，用于表示当前交易的状态。
     * <p>示例状态包括：
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
    private String tradeState;

    /**
     * 交易状态描述，提供对交易状态的详细说明。
     */
    private String tradeStateDesc;

    /**
     * 用户标识，标明进行支付的用户。
     */
    private String openid;

    /**
     * 用户支付金额（单位：分），表示用户实际支付的金额。
     */
    private Integer payerTotal;

    /**
     * 货币类型，标明交易所使用的货币。
     */
    private String currency;

    /**
     * 用户支付币种，标明用户支付时所使用的币种。
     */
    private String payerCurrency;

    /**
     * 接口返回内容，存储支付接口返回的原始内容。
     */
    private String content;

    /**
     * 总金额（单位：分），表示订单的总金额。
     */
    private Integer total;

    /**
     * 创建时间，记录支付信息的创建时间。
     */
    private Date createTime;

    /**
     * 更新时间，记录支付信息的最后更新时间。
     */
    private Date updateTime;

    /**
     * 支付完成时间，记录支付完成的具体时间。
     */
    private String successTime;
}
