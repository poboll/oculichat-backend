package com.caiths.caiapibackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caiths.caiapibackend.mapper.RechargeActivityMapper;
import com.caiths.caiapibackend.model.entity.RechargeActivity;
import com.caiths.caiapibackend.service.RechargeActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RechargeActivityServiceImpl 是充值活动服务的实现类。
 * <p>
 * 该类负责处理充值活动相关的业务逻辑，
 * 包括创建充值活动记录、查询充值活动等操作。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Service
public class RechargeActivityServiceImpl extends ServiceImpl<RechargeActivityMapper, RechargeActivity>
        implements RechargeActivityService {

    /**
     * 根据订单号获取充值活动列表。
     *
     * @param orderNo 订单号
     * @return {@link List}<{@link RechargeActivity}> 充值活动列表
     */
    @Override
    public List<RechargeActivity> getRechargeActivityByOrderNo(String orderNo) {
        LambdaQueryWrapper<RechargeActivity> activityLambdaQueryWrapper = new LambdaQueryWrapper<>();
        activityLambdaQueryWrapper.eq(RechargeActivity::getOrderNo, orderNo);
        return this.list(activityLambdaQueryWrapper);
    }
}
