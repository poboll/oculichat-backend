package com.caiths.oculichatback.controller;

import com.caiths.oculichatback.manager.FastDFSManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * FastDFS图片上传控制器，处理与图片上传相关的请求。
 *
 * @author poboll
 * @version 1.0
 * @since 2025-03-24
 */
@RestController
@RequestMapping("/fastdfs")
@Slf4j
public class FastDFSController {

    // 通过环境变量配置 FastDFS 主机访问地址，默认 "10.3.36.16:888"
    @Value("${FASTDFS_HOST:10.3.36.16:888}")
    private String fastdfsHost;

    /**
     * 上传文件接口，路径：/fastdfs/upload
     * 接收 MultipartFile 文件上传，调用 FastDFSManager.upload() 实现上传，返回完整访问 URL。
     */
//    @PostMapping("/upload")
//    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            String filePath = FastDFSManager.upload(file);
//            // 拼接完整访问 URL，例如：http://10.3.36.16:888/group1/M00/00/00/XXXXXXXXXXXX.jpg
//            String fileUrl = "http://" + fastdfsHost + "/" + filePath;
//            response.put("success", true);
//            response.put("fileUrl", fileUrl);
//            log.info("【FastDFSController】上传成功，文件 URL: {}", fileUrl);
//        } catch (IOException | MyException e) {
//            log.error("【FastDFSController】上传失败", e);
//            response.put("success", false);
//            response.put("error", e.getMessage());
//        }
//        return response;
//    }
    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 添加输入检查
            if (file == null || file.isEmpty()) {
                log.error("【FastDFS上传】文件为空");
                response.put("success", false);
                response.put("error", "上传文件为空");
                return response;
            }

            log.info("【FastDFS上传】接收到文件: 名称={}, 大小={}, 类型={}",
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType());
            log.info("【FastDFS上传】URL: {}", fastdfsHost);
            String filePath = FastDFSManager.upload(file);
            String fileUrl = "http://" + fastdfsHost + "/" + filePath;

            response.put("success", true);
            response.put("fileUrl", fileUrl);
            log.info("【FastDFS上传】上传成功，文件URL: {}", fileUrl);
        } catch (Exception e) {
            log.error("【FastDFS上传】上传失败", e);
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
}
