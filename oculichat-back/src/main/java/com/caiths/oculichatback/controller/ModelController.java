package com.caiths.oculichatback.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.caiths.oculichatback.common.ModelRequest;
import com.caiths.oculichatback.manager.FastDFSManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/model")
@Slf4j
public class ModelController {

    // 从环境变量中获取 FastDFS 主机地址，默认 "10.3.36.16:888"
    @Value("${FASTDFS_HOST:10.3.36.16:888}")
    private String fastdfsHost;

    /**
     * 模拟接口，供前端调试使用，不调用后端实际模型。
     */
    @PostMapping("/simulate")
    public Map<String, Object> simulatePredict(@RequestBody ModelRequest request) {
        log.info("【模拟接口】收到预测请求：{}", request);

        Map<String, Object> mainClass = new HashMap<>();
        mainClass.put("label", "Normal");
        mainClass.put("confidence", 0.971234);

        Map<String, Object> leftEye = new HashMap<>();
        leftEye.put("severity", "normal");
        leftEye.put("confidence", 0.945678);

        Map<String, Object> rightEye = new HashMap<>();
        rightEye.put("severity", "mild");
        rightEye.put("confidence", 0.823456);

        Map<String, Object> response = new HashMap<>();
        response.put("main_class", mainClass);
        response.put("left_eye", leftEye);
        response.put("right_eye", rightEye);
        response.put("age_prediction", 54);
        response.put("gender_prediction", "Male");

        long timestamp = Instant.now().toEpochMilli();
        String defaultImageUrl = "http://" + fastdfsHost + "/group1/M00/00/00/CgMkEGfCckyAbSVdAAJIWDya5Lg47.jpeg";
        String algorithmId = RandomStringUtils.randomAlphanumeric(12);
        log.info("【模拟接口】存储记录 - Timestamp: {}, Image URL: {}, Algorithm ID: {}",
                timestamp, defaultImageUrl, algorithmId);

        return response;
    }

    /**
     * 真实接口：上传左右眼 Base64 图片到 FastDFS，并转发请求到后端模型 API，
     * 同时记录当前时间戳、左右眼图片 URL及生成的算法唯一 ID（日志模拟存库）。
     */
    @PostMapping("/predict")
    public Map<String, Object> predict(@RequestBody ModelRequest request) {
        log.info("【真实接口】收到预测请求：{}", request);

        // 上传左眼图片（默认扩展名 jpg）
        String leftUrl = uploadBase64Image(request.getLeft_image_base64());
        // 上传右眼图片
        String rightUrl = uploadBase64Image(request.getRight_image_base64());
        if (leftUrl == null) {
            leftUrl = "http://" + fastdfsHost + "/group1/M00/00/00/CgMkEGfCckyAbSVdAAJIWDya5Lg47.jpeg";
        }
        if (rightUrl == null) {
            rightUrl = "http://" + fastdfsHost + "/group1/M00/00/00/CgMkEGfCckyAbSVdAAJIWDya5Lg47.jpeg";
        }

        long timestamp = Instant.now().toEpochMilli();
        String algorithmId = RandomStringUtils.randomAlphanumeric(12);
        log.info("【真实接口】存储记录 - Timestamp: {}, Left Image URL: {}, Right Image URL: {}, Algorithm ID: {}",
                timestamp, leftUrl, rightUrl, algorithmId);

        // 后端模型 API 地址（请根据实际部署情况修改）
        String backendUrl = "http://10.3.36.16:5000/predict";
        String jsonRequest = JSONUtil.toJsonStr(request);
        log.info("【真实接口】转发请求至后端模型 API：{}", jsonRequest);
        try {
            HttpResponse httpResponse = HttpRequest.post(backendUrl)
                    .header("Content-Type", "application/json")
                    .body(jsonRequest)
                    .timeout(20000)
                    .execute();
            String responseBody = httpResponse.body();
            log.info("【真实接口】后端模型 API 返回结果：{}", responseBody);
            Map<String, Object> result = JSONUtil.toBean(responseBody, Map.class);
            if (result.get("image_url") == null) {
                result.put("image_url", leftUrl + ";" + rightUrl);
            }
            return result;
        } catch (Exception e) {
            log.error("【真实接口】调用后端模型 API 失败", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "调用后端模型接口失败: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 将 Base64 编码字符串解码后上传至 FastDFS，返回文件访问 URL。
     * 此处直接调用 FastDFSManager.upload(byte[], "jpg")，无需使用 MockMultipartFile。
     */
    private String uploadBase64Image(String base64Str) {
        if (base64Str == null || base64Str.isEmpty()) {
            return null;
        }
        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64Str);
            String filePath = FastDFSManager.upload(imageBytes, "jpg");
            String fileUrl = "http://" + fastdfsHost + "/" + filePath;
            log.info("【真实接口】上传图片到 FastDFS 成功，返回 URL: {}", fileUrl);
            return fileUrl;
        } catch (Exception e) {
            log.error("【真实接口】上传图片到 FastDFS 失败", e);
            return null;
        }
    }
}
