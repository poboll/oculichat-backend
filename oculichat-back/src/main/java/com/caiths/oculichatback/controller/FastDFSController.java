package com.caiths.oculichatback.controller;

import com.caiths.oculichatback.manager.FastDFSManager;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    @PostMapping("/upload")
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            String filePath = FastDFSManager.upload(file);
            // 拼接完整访问 URL，例如：http://10.3.36.16:888/group1/M00/00/00/XXXXXXXXXXXX.jpg
            String fileUrl = "http://" + fastdfsHost + "/" + filePath;
            response.put("success", true);
            response.put("fileUrl", fileUrl);
            log.info("【FastDFSController】上传成功，文件 URL: {}", fileUrl);
        } catch (IOException | MyException e) {
            log.error("【FastDFSController】上传失败", e);
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
}
