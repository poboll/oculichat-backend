package com.caiths.caiapiinterface.utils;

import cn.hutool.http.HttpRequest;
import com.caiths.caiapisdk.exception.ApiException;
import com.caiths.caiapisdk.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * 请求工具类，提供构建URL和发送GET请求的方法。
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Slf4j
public class RequestUtils {

    /**
     * 生成带有查询参数的URL。
     *
     * @param baseUrl 基本URL
     * @param params  参数对象
     * @param <T>     参数类型
     * @return 构建后的URL字符串
     * @throws ApiException 构建URL时发生异常
     */
    public static <T> String buildUrl(String baseUrl, T params) throws ApiException {
        StringBuilder url = new StringBuilder(baseUrl);
        Field[] fields = params.getClass().getDeclaredFields();
        boolean isFirstParam = true;
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            // 跳过serialVersionUID属性
            if ("serialVersionUID".equals(name)) {
                continue;
            }
            try {
                Object value = field.get(params);
                if (value != null) {
                    if (isFirstParam) {
                        url.append("?").append(name).append("=").append(value);
                        isFirstParam = false;
                    } else {
                        url.append("&").append(name).append("=").append(value);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new ApiException(ErrorCode.OPERATION_ERROR, "构建URL异常");
            }
        }
        return url.toString();
    }

    /**
     * 发送带有参数的GET请求。
     *
     * @param baseUrl 基本URL
     * @param params  参数对象
     * @param <T>     参数类型
     * @return 响应内容
     * @throws ApiException 发送请求时发生异常
     */
    public static <T> String get(String baseUrl, T params) throws ApiException {
        return get(buildUrl(baseUrl, params));
    }

    /**
     * 发送GET请求。
     *
     * @param url 请求URL
     * @return 响应内容
     */
    public static String get(String url) {
        String body = HttpRequest.get(url).execute().body();
        log.info("【interface】：请求地址：{}，响应数据：{}", url, body);
        return body;
    }
}
