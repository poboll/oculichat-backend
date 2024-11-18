package com.caiths.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiths.caiapicommon.model.entity.UserInterfaceInfo;

public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    boolean hasInvokeNum(long userId, long interfaceInfoId);

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    public boolean invokeInterfaceCount(long interfaceInfoId, long userId);
}