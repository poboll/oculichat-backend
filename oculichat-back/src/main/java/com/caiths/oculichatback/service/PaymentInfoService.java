package com.caiths.oculichatback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiths.oculichatback.model.entity.PaymentInfo;
import com.caiths.oculichatback.model.vo.PaymentInfoVo;

/**
 * @Author: poboll
 * @Date: 2023/08/23 08:16:11
 * @Version: 1.0
 * @Description: 支付信息服务
 */
public interface PaymentInfoService extends IService<PaymentInfo> {
    /**
     * 创建付款信息
     *
     * @param paymentInfoVo 付款信息vo
     * @return boolean
     */
    boolean createPaymentInfo(PaymentInfoVo paymentInfoVo);
}
