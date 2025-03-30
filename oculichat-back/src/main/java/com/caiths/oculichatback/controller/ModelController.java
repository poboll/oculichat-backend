package com.caiths.oculichatback.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.caiths.oculichatback.common.ModelRequest;
import com.caiths.oculichatback.manager.FastDFSManager;
import com.caiths.oculichatback.utils.ImageProcessingUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/model")
@Slf4j
public class ModelController {

    // 从环境变量中获取 FastDFS 主机地址，默认 "10.3.36.16:888"
    @Value("${FASTDFS_HOST:10.3.36.16:888}")
    private String fastdfsHost;

    /**
     * 随机选择一组图片ID（471或3424）
     * @return 选择的图片ID字符串
     */
    private String getRandomImageSetId() {
        // 随机选择471或3424两组图片中的一组
        String[] imageSetIds = {"471", "3424"};
        int randomIndex = new Random().nextInt(imageSetIds.length);
        return imageSetIds[randomIndex];
    }

    /**
     * 根据图片ID和类型构建图片URL
     * @param eye 眼睛位置（"left"或"right"）
     * @param imageId 图片ID（471或3424）
     * @param imageType 图片类型（binary_map, contrast_enhanced等）
     * @return 图片的URL
     */
    private String buildImageUrl(String eye, String imageId, String imageType) {
        return "http://www.cdnjson.com/images/2025/03/30/" + eye + "_" + imageId + "_" + imageType + ".jpg";
    }

    /**
     * 模拟接口，供前端调试使用，不调用后端实际模型。
     * 新增了丰富的可解释性结果和可视化数据，随机返回两组图片中的一组
     */
    @PostMapping("/simulate")
    public Map<String, Object> simulatePredict(@RequestBody ModelRequest request) {
        log.info("【模拟接口】收到预测请求：{}", request);

        // 生成模拟结果
        Map<String, Object> response = new HashMap<>();

        // 1. 基础诊断结果
        Map<String, Object> mainClass = new HashMap<>();
        mainClass.put("label", "DR");
        mainClass.put("confidence", 0.971234);
        mainClass.put("grade", 2);

        Map<String, Object> leftEye = new HashMap<>();
        leftEye.put("severity", "moderate");
        leftEye.put("confidence", 0.945678);

        Map<String, Object> rightEye = new HashMap<>();
        rightEye.put("severity", "mild");
        rightEye.put("confidence", 0.823456);

        response.put("main_class", mainClass);
        response.put("left_eye", leftEye);
        response.put("right_eye", rightEye);
        response.put("age_prediction", 54);
        response.put("gender_prediction", "Male");

        // 2. 可解释性文本结果
        Map<String, Object> explanation = new HashMap<>();
        explanation.put("text", "诊断结果为DR二级，发现多个微血管瘤和出血灶。");
        List<String> findings = Arrays.asList("微血管瘤", "点状出血", "硬性渗出");
        explanation.put("findings", findings);
        response.put("explanation", explanation);

        // 3. 可视化结果 - 随机选择一组图片
        Map<String, Object> visualizations = new HashMap<>();

        // 随机选择图片ID（471或3424）
        String selectedImageId = getRandomImageSetId();
        log.info("【模拟接口】随机选择图片ID: {}", selectedImageId);

        // 左眼可视化
        Map<String, Object> leftEyeVis = new HashMap<>();
        leftEyeVis.put("original", buildImageUrl("left", selectedImageId, "original"));

        Map<String, String> leftFilteredViews = new HashMap<>();
        leftFilteredViews.put("green_channel", buildImageUrl("left", selectedImageId, "green_channel"));
        leftFilteredViews.put("red_free", buildImageUrl("left", selectedImageId, "red_free"));
        leftFilteredViews.put("contrast_enhanced", buildImageUrl("left", selectedImageId, "contrast_enhanced"));
        leftEyeVis.put("filtered_views", leftFilteredViews);

        leftEyeVis.put("probability_map", buildImageUrl("left", selectedImageId, "probability_map"));
        leftEyeVis.put("binary_map", buildImageUrl("left", selectedImageId, "binary_map"));

        // 如果有特征重要性图片，添加
        leftEyeVis.put("feature_importance", buildImageUrl("left", selectedImageId, "feature_importance"));

        // 右眼可视化
        Map<String, Object> rightEyeVis = new HashMap<>();
        rightEyeVis.put("original", buildImageUrl("right", selectedImageId, "original"));

        Map<String, String> rightFilteredViews = new HashMap<>();
        rightFilteredViews.put("green_channel", buildImageUrl("right", selectedImageId, "green_channel"));
        rightFilteredViews.put("red_free", buildImageUrl("right", selectedImageId, "red_free"));
        rightFilteredViews.put("contrast_enhanced", buildImageUrl("right", selectedImageId, "contrast_enhanced"));
        rightEyeVis.put("filtered_views", rightFilteredViews);

        rightEyeVis.put("probability_map", buildImageUrl("right", selectedImageId, "probability_map"));
        rightEyeVis.put("binary_map", buildImageUrl("right", selectedImageId, "binary_map"));

        // 如果3424还有gradcam特征图，添加
        if ("3424".equals(selectedImageId)) {
            rightEyeVis.put("gradcam_features", buildImageUrl("right", selectedImageId, "gradcam_features"));
        }

        visualizations.put("left_eye", leftEyeVis);
        visualizations.put("right_eye", rightEyeVis);
        response.put("visualizations", visualizations);

        // 4. 特征重要性
        Map<String, Object> featureImportance = new HashMap<>();
        featureImportance.put("image", buildImageUrl("left", selectedImageId, "feature_importance"));

        List<Map<String, Object>> factors = new ArrayList<>();
        Map<String, Object> factor1 = new HashMap<>();
        factor1.put("name", "微血管瘤");
        factor1.put("value", 0.42);
        factors.add(factor1);

        Map<String, Object> factor2 = new HashMap<>();
        factor2.put("name", "出血点");
        factor2.put("value", 0.35);
        factors.add(factor2);

        Map<String, Object> factor3 = new HashMap<>();
        factor3.put("name", "硬性渗出");
        factor3.put("value", 0.23);
        factors.add(factor3);

        featureImportance.put("factors", factors);
        response.put("feature_importance", featureImportance);

        // 5. 测量数据
        Map<String, Object> measurements = new HashMap<>();
        measurements.put("hemorrhage_count", 3);
        measurements.put("microaneurysm_count", 5);
        measurements.put("exudate_count", 2);
        response.put("measurements", measurements);

        // 记录模拟信息
        long timestamp = Instant.now().toEpochMilli();
        String defaultImageUrl = buildImageUrl("left", selectedImageId, "original") + ";" +
                buildImageUrl("right", selectedImageId, "original");
        String algorithmId = RandomStringUtils.randomAlphanumeric(12);
        log.info("【模拟接口】存储记录 - Timestamp: {}, Image URL: {}, Algorithm ID: {}",
                timestamp, defaultImageUrl, algorithmId);

        return response;
    }

    /**
     * 真实接口：上传左右眼 Base64 图片到 FastDFS，并转发请求到后端模型 API，
     * 同时处理眼底图像增强和可视化。
     */
    @PostMapping("/predict")
    public Map<String, Object> predict(@RequestBody ModelRequest request) {
        log.info("【真实接口】收到预测请求：{}", request);

        try {
            // 上传原始左右眼图片
            String leftUrl = uploadBase64Image(request.getLeft_image_base64());
            String rightUrl = uploadBase64Image(request.getRight_image_base64());

            if (leftUrl == null) {
                leftUrl = "http://" + fastdfsHost + "/group1/M00/00/00/CgMkEGfCckyAbSVdAAJIWDya5Lg47.jpeg";
            }
            if (rightUrl == null) {
                rightUrl = "http://" + fastdfsHost + "/group1/M00/00/00/CgMkEGfCckyAbSVdAAJIWDya5Lg47.jpeg";
            }

            // 处理图像并生成增强版本
            Map<String, String> leftEyeEnhanced = processEyeImage(request.getLeft_image_base64());
            Map<String, String> rightEyeEnhanced = processEyeImage(request.getRight_image_base64());

            // 记录请求信息
            long timestamp = Instant.now().toEpochMilli();
            String algorithmId = RandomStringUtils.randomAlphanumeric(12);
            log.info("【真实接口】存储记录 - Timestamp: {}, Left Image URL: {}, Right Image URL: {}, Algorithm ID: {}",
                    timestamp, leftUrl, rightUrl, algorithmId);

            // 后端模型 API 地址（请根据实际部署情况修改）
            String backendUrl = "http://10.3.36.16:5000/predict";

            // 扩展原始请求，添加额外参数
            Map<String, Object> enhancedRequest = new HashMap<>();
            enhancedRequest.put("left_image_base64", request.getLeft_image_base64());
            enhancedRequest.put("right_image_base64", request.getRight_image_base64());
            enhancedRequest.put("patient_age", request.getPatient_age());
            enhancedRequest.put("patient_gender", request.getPatient_gender());
            enhancedRequest.put("need_explanation", true);  // 指示后端生成可解释性结果

            String jsonRequest = JSONUtil.toJsonStr(enhancedRequest);
            log.info("【真实接口】转发增强请求至后端模型 API");

            HttpResponse httpResponse = HttpRequest.post(backendUrl)
                    .header("Content-Type", "application/json")
                    .body(jsonRequest)
                    .timeout(20000)
                    .execute();

            String responseBody = httpResponse.body();
            log.info("【真实接口】后端模型 API 返回结果长度: {}", responseBody.length());

            Map<String, Object> modelResult = JSONUtil.toBean(responseBody, Map.class);

            // 合并模型结果与图像处理结果
            Map<String, Object> finalResult = new HashMap<>(modelResult);

            // 如果模型没有返回可视化结果，添加我们生成的
            if (!modelResult.containsKey("visualizations")) {
                Map<String, Object> visualizations = new HashMap<>();

                // 左眼可视化
                Map<String, Object> leftEyeVis = new HashMap<>();
                leftEyeVis.put("original", leftUrl);
                leftEyeVis.put("filtered_views", leftEyeEnhanced);

                // 右眼可视化
                Map<String, Object> rightEyeVis = new HashMap<>();
                rightEyeVis.put("original", rightUrl);
                rightEyeVis.put("filtered_views", rightEyeEnhanced);

                visualizations.put("left_eye", leftEyeVis);
                visualizations.put("right_eye", rightEyeVis);

                finalResult.put("visualizations", visualizations);
            }

            // 添加原始图片URL
            finalResult.put("image_url", leftUrl + ";" + rightUrl);

            return finalResult;

        } catch (Exception e) {
            log.error("【真实接口】处理失败", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "调用后端模型接口失败: " + e.getMessage());
            return errorResult;
        }
    }

    /**
     * 处理眼底图像并生成各种增强视图
     * @param base64Str 原始Base64图像
     * @return 各种增强视图的URL映射
     */
    private Map<String, String> processEyeImage(String base64Str) {
        Map<String, String> enhancedViews = new HashMap<>();

        try {
            if (base64Str == null || base64Str.isEmpty()) {
                return enhancedViews;
            }

            byte[] imageBytes = Base64.getDecoder().decode(base64Str);

            // 绿通道图像处理
            byte[] greenChannelImage = ImageProcessingUtils.extractGreenChannel(imageBytes);
            String greenChannelUrl = uploadBase64Image(Base64.getEncoder().encodeToString(greenChannelImage));
            enhancedViews.put("green_channel", greenChannelUrl);

            // 无红图像处理
            byte[] redFreeImage = ImageProcessingUtils.createRedFreeImage(imageBytes);
            String redFreeUrl = uploadBase64Image(Base64.getEncoder().encodeToString(redFreeImage));
            enhancedViews.put("red_free", redFreeUrl);

            // 对比度增强图像处理
            byte[] contrastEnhancedImage = ImageProcessingUtils.enhanceContrast(imageBytes);
            String contrastEnhancedUrl = uploadBase64Image(Base64.getEncoder().encodeToString(contrastEnhancedImage));
            enhancedViews.put("contrast_enhanced", contrastEnhancedUrl);

            return enhancedViews;
        } catch (Exception e) {
            log.error("【真实接口】处理眼底图像增强失败", e);
            return enhancedViews;
        }
    }

    /**
     * 将 Base64 编码字符串解码后上传至 FastDFS，返回文件访问 URL。
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
