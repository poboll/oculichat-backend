package com.caiths.caiapibackend.model.vo;

import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 支付信息的值对象，用于封装支付相关的详细信息。
 * <p>
 * 包含支付应用 ID、商户 ID、交易编号、交易状态等信息，以及支付金额、场景信息和促销详情等。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
@NoArgsConstructor
public class PaymentInfoVo {
    private static final long serialVersionUID = 1L;

    /**
     * 支付应用 ID。
     */
    private String appid;

    /**
     * 商户 ID。
     */
    private String mchid;

    /**
     * 商户订单号。
     */
    private String outTradeNo;

    /**
     * 微信交易号。
     */
    private String transactionId;

    /**
     * 贸易类型。
     */
    private String tradeType;

    /**
     * 贸易状态。
     */
    private String tradeState;

    /**
     * 贸易状态描述。
     */
    private String tradeStateDesc;

    /**
     * 银行类型。
     */
    private String bankType;

    /**
     * 附加数据。
     */
    private String attach;

    /**
     * 支付成功时间。
     */
    private String successTime;

    /**
     * 支付者信息。
     */
    private WxPayOrderQueryV3Result.Payer payer;

    /**
     * 支付金额信息。
     */
    @SerializedName(value = "amount")
    private WxPayOrderQueryV3Result.Amount amount;

    /**
     * 场景信息。
     */
    @SerializedName(value = "scene_info")
    private WxPayOrderQueryV3Result.SceneInfo sceneInfo;

    /**
     * 促销详情列表。
     */
    @SerializedName(value = "promotion_detail")
    private List<WxPayOrderQueryV3Result.PromotionDetail> promotionDetails;
}
