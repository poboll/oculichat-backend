package com.caiths.oculichatback.manager;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.caiths.oculichatback.config.CosClientConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * 提供对 COS（对象存储服务）的操作，包括上传对象等功能。
 * <p>
 * 该类使用腾讯云 COS SDK 进行对象存储的交互。
 * </p>
 *
 * @author poboll
 * @version 1.0
 * @since 2024-12-02
 */
@Component
public class CosManager {

    /**
     * COS 客户端配置，包含存储桶名称等配置信息。
     */
    @Resource
    private CosClientConfig cosClientConfig;

    /**
     * 腾讯云 COS 客户端，用于执行对象存储操作。
     */
    @Resource
    private COSClient cosClient;

    /**
     * 上传本地文件到 COS 存储。
     * <p>
     * 根据指定的唯一键和本地文件路径，将文件上传到配置的存储桶中。
     * </p>
     *
     * @param key           对象在存储桶中的唯一标识
     * @param localFilePath 本地文件的绝对路径
     * @return 上传操作的结果，包含如 ETag 等信息
     */
    public PutObjectResult putObject(String key, String localFilePath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                cosClientConfig.getBucket(),
                key,
                new File(localFilePath)
        );
        return cosClient.putObject(putObjectRequest);
    }

    /**
     * 上传文件到 COS 存储。
     * <p>
     * 根据指定的唯一键和文件对象，将文件上传到配置的存储桶中。
     * </p>
     *
     * @param key  对象在存储桶中的唯一标识
     * @param file 要上传的文件对象
     * @return 上传操作的结果，包含如 ETag 等信息
     */
    public PutObjectResult putObject(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(
                cosClientConfig.getBucket(),
                key,
                file
        );
        return cosClient.putObject(putObjectRequest);
    }
}
