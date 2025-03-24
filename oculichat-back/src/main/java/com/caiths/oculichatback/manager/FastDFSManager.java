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
 * @since 2025-03-24
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
//static {
//    try {
//        // 修改方式1：使用ClassPathResource直接获取配置文件路径
////        ClassPathResource resource = new ClassPathResource("fastdfs-client.properties");
////        ClientGlobal.init(resource.getFile().getAbsolutePath());
//
//        // 或者修改方式2：手动创建properties并设置配置项
////        /*
//        Properties props = new Properties();
//        props.put("fastdfs.tracker_servers", "10.3.36.16:22122");
//        props.put("fastdfs.connect_timeout_in_seconds", "5");
//        props.put("fastdfs.network_timeout_in_seconds", "30");
//        props.put("fastdfs.charset", "UTF-8");
//        ClientGlobal.initByProperties(props);
////        */
//
//        TrackerClient trackerClient = new TrackerClient();
//        TrackerServer trackerServer = trackerClient.getConnection();
//        // 添加连接检查
//        if (trackerServer == null) {
//            throw new MyException("无法连接到TrackerServer，请检查网络和配置");
//        }
//        client1 = new StorageClient1(trackerServer, null);
//
//        // 记录初始化成功信息
////        System.out.println("FastDFS客户端初始化成功，Tracker服务器: " + ClientGlobal.getG_tracker_group().getTrackerServer(0).getInetSocketAddress().toString());
//    } catch (IOException e) {
//        e.printStackTrace();
//        System.err.println("FastDFS初始化IO异常: " + e.getMessage());
//    } catch (MyException e) {
//        e.printStackTrace();
//        System.err.println("FastDFS初始化异常: " + e.getMessage());
//    }
//}

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
