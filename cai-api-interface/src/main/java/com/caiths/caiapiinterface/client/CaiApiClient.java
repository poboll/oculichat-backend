package com.caiths.caiapiinterface.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.caiths.caiapiinterface.model.User;
import com.caiths.caiapiinterface.util.SignUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author poboll
 * @Date 2024年10月22日 20:38
 * @Version 1.0
 * @Description 调用第三方接口的客户端
 */
public class CaiApiClient {
    private String accessKey;
    private String secretKey;
    public CaiApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }
    public String getNameByGet(String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result= HttpUtil.get("http://localhost:8123/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    public String getNameByPost(@RequestParam String name) {
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);

        String result= HttpUtil.post("http://localhost:8123/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    private Map<String, String> getHeaderMap(String body) throws UnsupportedEncodingException {
        Map<String, String> hashMap = new HashMap<>();

        hashMap.put("accessKey", accessKey);
        // 不能直接发送给后端
        // hashMap.put("secretKey", secretKey);
        hashMap.put("sign", SignUtil.genSign(body, secretKey));
        // 防止中文乱码
        hashMap.put("body", URLEncoder.encode(body, StandardCharsets.UTF_8.name()));
        hashMap.put("nonce", RandomUtil.randomNumbers(5));
        hashMap.put("timestamp", String.valueOf(System.currentTimeMillis()));

        return hashMap;
    }

    public String getUsernameByPost(@RequestBody User user) throws UnsupportedEncodingException {
        // 将对象转换为 JSON 字符串
        String json = JSONUtil.toJsonStr(user);

        // 获取所有的请求头信息
        Map<String, String> headers = getHeaderMap(json);

        // 输出 POST 请求的所有参数
        System.out.println("POST 请求 Headers: " + headers);
        System.out.println("POST 请求 Body: " + json);

        // 发送 POST 请求
        HttpResponse response = HttpRequest.post("http://localhost:8123/api/name/user/")
                .charset(StandardCharsets.UTF_8) // 设置字符集编码
                .addHeaders(headers) // 添加请求头
                .body(json) // 添加请求体
                .execute(); // 执行HTTP请求

        // 输出响应结果
        //System.out.println("response = " + response);
        // System.out.println("status = " + response.getStatus());

        // 返回响应体内容
        if (response.isOk()) {
            return response.body();
        }
        return "fail";
    }
}
