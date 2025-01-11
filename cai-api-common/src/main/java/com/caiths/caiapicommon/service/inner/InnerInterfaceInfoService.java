package com.caiths.caiapicommon.service.inner;

import com.caiths.caiapicommon.model.entity.InterfaceInfo;

/**
 * 接口信息服务，提供与接口信息相关的操作。
 *
 * <p>该服务用于获取和管理系统中各个接口的详细信息。</p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public interface InnerInterfaceInfoService {

    /**
     * 获取指定路径和方法的接口信息。
     *
     * @param path   接口的路径，例如 "/api/v1/users"
     * @param method 接口的请求方法，例如 "GET"、"POST"
     * @return {@link InterfaceInfo} 指定路径和方法对应的接口信息
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
