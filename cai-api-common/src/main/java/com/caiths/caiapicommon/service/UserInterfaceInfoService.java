package com.caiths.caiapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiths.caiapicommon.model.entity.UserInterfaceInfo;

/**
 * 内部用户接口调用信息服务，提供对用户接口调用次数的管理和查询功能。
 * 此服务接口包含检查用户接口调用次数和记录调用次数的功能，用于限制用户对接口的调用次数并进行相应的计数。
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 检查用户是否还有剩余的接口调用次数。
     * 根据用户ID（userId）和接口ID（interfaceInfoId）查询当前用户是否还有可用的接口调用次数。
     * 若调用次数已耗尽，则返回 false。
     *
     * @param userId          用户ID
     * @param interfaceInfoId 接口ID
     * @return boolean 是否有剩余的调用次数；如果有，返回 true；否则返回 false
     */
    boolean hasInvokeNum(long userId, long interfaceInfoId);

    /**
     * 验证用户接口调用信息是否合法。
     *
     * @param userInterfaceInfo 用户接口信息
     * @param add 是否为添加操作
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 记录用户对接口的调用次数（调用一次计数加1）。
     * 根据用户ID和接口ID更新用户的接口调用次数，调用一次则计数增加1。
     *
     * @param userId          用户ID
     * @param interfaceInfoId 接口ID
     * @return boolean 是否成功更新调用次数；成功返回 true，失败返回 false
     */
    boolean invokeInterfaceCount(long userId, long interfaceInfoId);
}