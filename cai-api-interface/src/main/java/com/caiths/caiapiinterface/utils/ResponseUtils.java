package com.caiths.caiapiinterface.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.caiths.swiftweb.common.ErrorCode;
import com.caiths.swiftweb.exception.BusinessException;
import com.caiths.caiapisdk.exception.ApiException;
import com.caiths.caiapisdk.model.response.ResultResponse;

import java.util.Map;

import static com.caiths.caiapiinterface.utils.RequestUtils.get;

/**
 * 响应工具类，提供处理API响应的方法。
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
public class ResponseUtils {

    /**
     * 将响应字符串转换为Map。
     *
     * @param response 响应字符串
     * @return {@link Map} 转换后的Map对象
     */
    public static Map<String, Object> responseToMap(String response) {
        return new Gson().fromJson(response, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * 基础响应处理，封装请求和响应逻辑。
     *
     * @param baseUrl 基本URL
     * @param params  参数对象
     * @param <T>     参数类型
     * @return {@link ResultResponse} 处理后的响应
     * @throws BusinessException 处理过程中发生业务异常
     */
    public static <T> ResultResponse baseResponse(String baseUrl, T params) {
        String response = null;
        try {
            response = get(baseUrl, params);
            Map<String, Object> fromResponse = responseToMap(response);
            boolean success = (boolean) fromResponse.get("success");
            ResultResponse baseResponse = new ResultResponse();
            if (!success) {
                baseResponse.setData(fromResponse);
                return baseResponse;
            }
            fromResponse.remove("success");
            baseResponse.setData(fromResponse);
            return baseResponse;
        } catch (ApiException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "构建URL异常");
        }
    }
}
