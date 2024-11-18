package com.caiths.caiapicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caiths.caiapicommon.model.entity.User;

/**
 * 内部用户信息服务接口，用于提供基于密钥的用户查询功能。
 * 此服务接口主要用于通过用户的访问密钥（accessKey）查询用户信息。
 */
public interface InnerUserService {

    /**
     * 根据访问密钥（accessKey）查询用户信息。
     * 通过给定的访问密钥在数据库中查找并返回对应的用户信息，用于验证用户身份或授权。
     * 如果未找到匹配的用户，则返回 null。
     *
     * @param accessKey 用户的访问密钥
     * @return User 查询到的用户信息；如果用户不存在，则返回 null
     */
    User getInvokeUser(String accessKey);
}