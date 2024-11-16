package com.caiths.caiapicommon.service;

import com.caiths.caiapicommon.model.entity.InterfaceInfo;

/**
 * 内部接口信息服务，用于提供对接口信息的查询支持。
 * 此服务接口用于根据请求路径和请求方法查找对应的接口信息，返回接口信息以便后续调用。
 */
public interface InnerInterfaceInfoService {

    /**
     * 根据请求路径和请求方法查询接口信息。
     * 此方法会根据传入的请求路径（path）和请求方法（method）在数据库中查找匹配的接口信息，
     * 并返回该接口的详细信息。若接口信息不存在，则返回空。
     *
     * @param path   请求路径，允许 "/api/v1/resource"
     * @param method 请求方法，允许 "GET" 或 "POST"
     * @return InterfaceInfo 对应接口的详细信息；如果不存在，则返回 null
     */
    InterfaceInfo getInvokeInterfaceInfo(String path, String method);
}