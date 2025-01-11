package com.caiths.caiapibackend.service.inner.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.caiths.caiapibackend.common.ErrorCode;
import com.caiths.caiapibackend.exception.BusinessException;
import com.caiths.caiapibackend.model.entity.User;
import com.caiths.caiapibackend.service.UserService;
import com.caiths.caiapicommon.model.vo.UserVO;
import com.caiths.caiapicommon.service.inner.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;

/**
 * @Author: poboll
 * @Date: 2023年09月15日 22:54
 * @Version: 1.0
 * @Description:
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserService userService;

    @Override
    public UserVO getInvokeUserByAccessKey(String accessKey) {
        if (StringUtils.isAnyBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getAccessKey, accessKey);
        User user = userService.getOne(userLambdaQueryWrapper);
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
