package com.caiths.oculichatinterface.controller;

import cn.hutool.json.JSONUtil;
import com.caiths.caiapisdk.exception.ApiException;
import com.caiths.caiapisdk.model.params.*;
import com.caiths.caiapisdk.model.response.NameResponse;
import com.caiths.caiapisdk.model.response.RandomWallpaperResponse;
import com.caiths.caiapisdk.model.response.ResultResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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
}
