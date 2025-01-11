package com.caiths.caiapibackend.model.dto.pay;

import lombok.Data;

import java.io.Serializable;

/**
 * 付款创建请求数据传输对象。
 * <p>
 * 该类用于封装创建付款请求所需的参数，包括产品的唯一标识符和支付类型。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Data
public class PayCreateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 产品的唯一标识符，用于指定付款所针对的产品。
     */
    private String productId;

    /**
     * 支付类型，指明付款的方式（例如：信用卡、支付宝、微信支付等）。
     */
    private String payType;
}
