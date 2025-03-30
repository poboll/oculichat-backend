package com.caiths.oculichatinterface.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.caiths.caiapisdk.exception.ApiException;
import com.caiths.caiapisdk.model.params.*;
import com.caiths.caiapisdk.model.response.NameResponse;
import com.caiths.caiapisdk.model.response.RandomWallpaperResponse;
import com.caiths.caiapisdk.model.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.caiths.oculichatinterface.utils.RequestUtils.buildUrl;
import static com.caiths.oculichatinterface.utils.RequestUtils.get;
import static com.caiths.oculichatinterface.utils.ResponseUtils.baseResponse;
import static com.caiths.oculichatinterface.utils.ResponseUtils.responseToMap;

/**
 * 服务控制器，处理各种API请求。
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@RestController
@RequestMapping("/")
@Slf4j
public class ServiceController {

    private static final String AI_STUDIO_API_URL = "https://edpeff67cd363ff6.aistudio-hub.baidu.com/chat/completions";
    private static final String AI_STUDIO_AUTH_TOKEN = "2cdb31eed534342e5a2bf310927ef8ad208232ab";

    /**
     * 获取姓名信息。
     *
     * @param nameParams 姓名参数
     * @return {@link NameResponse} 姓名响应
     */
    @GetMapping("/name")
    public NameResponse getName(NameParams nameParams) {
        return JSONUtil.toBean(JSONUtil.toJsonStr(nameParams), NameResponse.class);
    }

    /**
     * 获取随机情话。
     *
     * @return {@link String} 随机情话
     */
    @GetMapping("/loveTalk")
    public String randomLoveTalk() {
        return get("https://api.vvhan.com/api/love");
    }

    /**
     * 获取毒鸡汤。
     *
     * @return {@link String} 毒鸡汤内容
     */
    @GetMapping("/poisonousChickenSoup")
    public String getPoisonousChickenSoup() {
        return get("https://api.btstu.cn/yan/api.php?charset=utf-8&encode=json");
    }

    /**
     * 获取随机壁纸（GET请求）。
     *
     * @param randomWallpaperParams 随机壁纸参数
     * @return {@link RandomWallpaperResponse} 随机壁纸响应
     * @throws ApiException API异常
     */
    @GetMapping("/randomWallpaper")
    public RandomWallpaperResponse randomWallpaper(RandomWallpaperParams randomWallpaperParams) throws ApiException {
        String baseUrl = "https://api.btstu.cn/sjbz/api.php";
        String url = buildUrl(baseUrl, randomWallpaperParams);
        if (StringUtils.isAllBlank(randomWallpaperParams.getLx(), randomWallpaperParams.getMethod())) {
            url = url + "?format=json";
        } else {
            url = url + "&format=json";
        }
        return JSONUtil.toBean(get(url), RandomWallpaperResponse.class);
    }

    /**
     * 获取随机壁纸（POST请求）。
     *
     * @param randomWallpaperParams 随机壁纸参数
     * @return {@link RandomWallpaperResponse} 随机壁纸响应
     * @throws ApiException API异常
     */
    @PostMapping("/randomWallpaper")
    public RandomWallpaperResponse postRandomWallpaper(@RequestBody RandomWallpaperParams randomWallpaperParams) throws ApiException {
        String baseUrl = "https://api.btstu.cn/sjbz/api.php";
        String url = buildUrl(baseUrl, randomWallpaperParams);
        if (StringUtils.isAllBlank(randomWallpaperParams.getLx(), randomWallpaperParams.getMethod())) {
            url = url + "?format=json";
        } else {
            url = url + "&format=json";
        }
        return JSONUtil.toBean(get(url), RandomWallpaperResponse.class);
    }

    /**
     * 获取星座运势。
     *
     * @param horoscopeParams 星座参数
     * @return {@link ResultResponse} 星座运势响应
     * @throws ApiException API异常
     */
    @GetMapping("/horoscope")
    public ResultResponse getHoroscope(HoroscopeParams horoscopeParams) throws ApiException {
        String response = get("https://api.vvhan.com/api/horoscope", horoscopeParams);
        Map<String, Object> fromResponse = responseToMap(response);
        boolean success = (boolean) fromResponse.get("success");
        if (!success) {
            ResultResponse baseResponse = new ResultResponse();
            baseResponse.setData(fromResponse);
            return baseResponse;
        }
        return JSONUtil.toBean(response, ResultResponse.class);
    }

    /**
     * 获取IP信息。
     *
     * @param ipInfoParams IP信息参数
     * @return {@link ResultResponse} IP信息响应
     */
    @GetMapping("/ipInfo")
    public ResultResponse getIpInfo(IpInfoParams ipInfoParams) {
        return baseResponse("https://api.vvhan.com/api/getIpInfo", ipInfoParams);
    }

    /**
     * 获取天气信息。
     *
     * @param weatherParams 天气参数
     * @return {@link ResultResponse} 天气信息响应
     */
    @GetMapping("/weather")
    public ResultResponse getWeatherInfo(WeatherParams weatherParams) {
        return baseResponse("https://api.vvhan.com/api/weather", weatherParams);
    }

    /**
     * AI问询接口，代理百度AI Studio的API请求。
     *
     * @param requestBody 请求体，包含用户问题
     * @return AI响应结果
     */
    @PostMapping("/inquiry")
    public Object aiInquiry(@RequestBody Map<String, Object> requestBody) {
        try {
            log.info("AI inquiry request: {}", JSONUtil.toJsonStr(requestBody));

            // 构建请求体
            Map<String, Object> aiRequestBody = new HashMap<>();

            // 如果客户端直接传入了完整的messages结构，直接使用
            if (requestBody.containsKey("messages") && requestBody.get("messages") instanceof List) {
                aiRequestBody.put("messages", requestBody.get("messages"));
            } else {
                // 否则构建messages结构
                String userContent = requestBody.containsKey("content")
                        ? requestBody.get("content").toString()
                        : "你好";

                // 修复 List.of 和 Map.of 不可用的问题
                List<Map<String, Object>> messages = new ArrayList<>();
                Map<String, Object> userMessage = new HashMap<>();
                userMessage.put("role", "user");
                userMessage.put("content", userContent);
                messages.add(userMessage);

                aiRequestBody.put("messages", messages);
            }

            // 添加其他可能的参数
            if (requestBody.containsKey("temperature")) {
                aiRequestBody.put("temperature", requestBody.get("temperature"));
            }
            if (requestBody.containsKey("max_tokens")) {
                aiRequestBody.put("max_tokens", requestBody.get("max_tokens"));
            }

            // 发送请求到百度AI Studio
            String response = HttpRequest.post(AI_STUDIO_API_URL)
                    .header("Authorization", AI_STUDIO_AUTH_TOKEN)
                    .header("Content-Type", "application/json")
                    .body(JSONUtil.toJsonStr(aiRequestBody))
                    .execute()
                    .body();

            log.info("AI inquiry response: {}", response);

            // 解析响应并返回
            return JSONUtil.parse(response);
        } catch (Exception e) {
            log.error("Error in AI inquiry", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", true);
            errorResponse.put("message", "AI inquiry failed: " + e.getMessage());
            return errorResponse;
        }
    }
}
