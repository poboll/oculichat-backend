package com.caiths.caiapibackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.caiths.caiapibackend.common.BaseResponse;
import com.caiths.caiapibackend.common.ErrorCode;
import com.caiths.caiapibackend.common.ResultUtils;
import com.caiths.caiapibackend.exception.BusinessException;
import com.caiths.caiapibackend.model.entity.DailyCheckIn;
import com.caiths.caiapibackend.model.vo.UserVO;
import com.caiths.caiapibackend.service.DailyCheckInService;
import com.caiths.caiapibackend.service.UserService;
import com.caiths.caiapibackend.utils.RedissonLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 签到控制器，处理与用户签到相关的请求。
 * <p>
 * 提供用户每日签到的接口，包括检查用户是否已签到、记录签到信息以及更新用户钱包余额。
 * </p>
 *
 * <p>此控制器类包含与用户每日签到相关的所有端点，如签到操作。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@RestController
@RequestMapping("/dailyCheckIn")
@Slf4j
public class DailyCheckInController {

    @Resource
    private DailyCheckInService dailyCheckInService;

    @Resource
    private UserService userService;

    @Resource
    private RedissonLockUtil redissonLockUtil;

    // region 增删改查

    /**
     * 用户每日签到接口。
     * <p>
     * 该方法处理用户的签到请求，确保同一用户在同一日内只能签到一次。
     * 成功签到后，系统将为用户添加积分并更新用户的钱包余额。
     * </p>
     *
     * @param request 当前的 HTTP 请求，包含用户的认证信息
     * @return {@link BaseResponse} 包含操作结果的响应对象
     *         成功时返回 {@code true}，表示签到成功
     * @throws BusinessException 当用户已签到或操作失败时抛出业务异常
     */
    @PostMapping("/doCheckIn")
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Boolean> doDailyCheckIn(HttpServletRequest request) {
        // 获取当前登录用户信息
        UserVO loginUser = userService.getLoginUser(request);
        // 定义分布式锁的名称，确保同一用户的签到操作是互斥的
        String redissonLock = ("doDailyCheckIn_" + loginUser.getUserAccount()).intern();

        // 使用 Redisson 分布式锁执行签到逻辑
        return redissonLockUtil.redissonDistributedLocks(redissonLock, () -> {
            // 构建查询条件，查找用户当天是否已签到
            LambdaQueryWrapper<DailyCheckIn> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DailyCheckIn::getUserId, loginUser.getId());
            DailyCheckIn dailyCheckIn = dailyCheckInService.getOne(queryWrapper);

            // 如果用户已签到，抛出业务异常
            if (ObjectUtils.isNotEmpty(dailyCheckIn)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "签到失败，今日已签到");
            }

            // 创建新的签到记录
            dailyCheckIn = new DailyCheckIn();
            dailyCheckIn.setUserId(loginUser.getId());
            dailyCheckIn.setAddPoints(10);
            boolean checkInSaved = dailyCheckInService.save(dailyCheckIn);

            // 更新用户钱包余额
            boolean walletUpdated = userService.addWalletBalance(loginUser.getId(), dailyCheckIn.getAddPoints());

            // 检查所有操作是否成功
            boolean success = checkInSaved && walletUpdated;
            if (!success) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "签到过程出现错误");
            }

            // 返回成功响应
            return ResultUtils.success(true);
        }, "签到失败");
    }

    // endregion
}
