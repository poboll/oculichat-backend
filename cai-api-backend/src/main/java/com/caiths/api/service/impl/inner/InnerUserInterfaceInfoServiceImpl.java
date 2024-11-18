package com.caiths.api.service.impl.inner;

import com.caiths.api.service.UserInterfaceInfoService;
import com.caiths.caiapicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
//
//    @Override
//    public boolean hasInvokeNum(long userId, long interfaceInfoId) {
//        // TODO: 添加具体逻辑
//        return userInterfaceInfoService.hasInvokeNum(userId, interfaceInfoId);
//    }
//
//    @Override
//    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
//        // TODO: 添加具体逻辑
//        if (userInterfaceInfo == null) {
//            throw new IllegalArgumentException("用户接口信息不能为空");
//        }
//        // 其他校验逻辑
//    }

    @Override
    public boolean invokeInterfaceCount(long userId, long interfaceInfoId) {
        return userInterfaceInfoService.invokeInterfaceCount(userId, interfaceInfoId);
    }
}