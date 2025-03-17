package com.caiths.oculichatback.model.dto.productorder;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求数据传输对象。
 * <p>
 * 该类用于封装创建产品订单所需的参数，包括产品ID、支付类型以及订单号。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class ProductOrderAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品的唯一标识符，用于指定订单所对应的产品。
     */
    private String productId;

    /**
     * 支付类型，指明订单的支付方式。
     * <p>示例类型包括：WX-微信支付、ZFB-支付宝支付。</p>
     */
    private String payType;

    /**
     * 订单号，用于唯一标识每一个订单。
     */
    private String orderNo;
}
