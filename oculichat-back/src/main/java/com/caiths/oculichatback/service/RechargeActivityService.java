package com.caiths.oculichatback.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiths.oculichatback.model.entity.RechargeActivity;

import java.util.List;

/**
 * @Author: poboll
 * @Date: 2023/09/11 11:26:42
 * @Version: 1.0
 * @Description: 充值活动服务
 */
public interface RechargeActivityService extends IService<RechargeActivity> {
    /**
     * 按订单号获取充值活动
     *
     * @param orderNo 订单号
     * @return {@link RechargeActivity}
     */
    List<RechargeActivity> getRechargeActivityByOrderNo(String orderNo);
}
