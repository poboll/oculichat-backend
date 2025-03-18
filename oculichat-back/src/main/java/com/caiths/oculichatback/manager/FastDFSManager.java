package com.caiths.oculichatback.manager;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * FastDFSManager 文件操作工具类。
 *
 * 该类封装了 FastDFS 文件上传功能，提供将 MultipartFile 或字节数组上传到分布式文件系统的方法。
 *
 * 需要在 resources 下配置 fastdfs-client.properties 文件，例如：
 *   tracker_servers=10.3.36.16:22122
 *   connect_timeout_in_seconds=5
 *   network_timeout_in_seconds=30
 *   charset=UTF-8
 *
 * 预览图片时，访问地址格式为：http://FASTDFS_HOST/ + 文件存储路径
 * FASTDFS_HOST 可通过环境变量配置，默认示例为 "10.3.36.16:888"
 *
 * @author poboll
 * @since 2025-02-26
 */
public class FastDFSManager {

    private static StorageClient1 client1;

    static {
        try {
            // 初始化 FastDFS 全局配置，加载 fastdfs-client.properties 文件
            ClientGlobal.initByProperties("fastdfs-client.properties");
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            client1 = new StorageClient1(trackerServer, null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传 MultipartFile 文件到 FastDFS。
     *
     * @param file 要上传的文件，类型为 MultipartFile
     * @return 上传后文件的存储路径，如 "group1/M00/00/00/XXXXXXXXXXXX.jpg"
     * @throws IOException 如果文件读取失败
     * @throws MyException 如果 FastDFS 操作发生异常
     */
    public static String upload(MultipartFile file) throws IOException, MyException {
        String oldName = file.getOriginalFilename();
        String ext = oldName.substring(oldName.lastIndexOf(".") + 1);
        return client1.upload_file1(file.getBytes(), ext, null);
    }

    /**
     * 上传字节数组到 FastDFS。
     *
     * @param fileBytes 文件内容的字节数组
     * @param ext       文件扩展名，例如 "jpg"
     * @return 上传后文件的存储路径，如 "group1/M00/00/00/XXXXXXXXXXXX.jpg"
     * @throws IOException 如果读取字节数组失败
     * @throws MyException 如果 FastDFS 操作发生异常
     */
    public static String upload(byte[] fileBytes, String ext) throws IOException, MyException {
        return client1.upload_file1(fileBytes, ext, null);
    }
}
