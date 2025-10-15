package com.imp.all.file.core;

import com.imp.all.file.model.Bucket;
import com.imp.all.file.model.FileTaskInfoV5;
import com.imp.all.file.netty.FileSocketClient;
import com.imp.all.file.request.DownloadRequestV5;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件SDK</br> 初始化,获取 FileManage , release
 */
@Slf4j
public class FileSDK {

    private static final String username = "lill169";
    private static volatile String HOST;
    private static volatile int PORT;
    public static volatile Bucket envBucket;

    private static final Map<String, String> uriMap = new HashMap<String, String>() {{
        put("sit", "https://mapnew5sit.midea.com:6101");
        put("sit-red", "https://mapfilered5sit.midea.com:6101");
        put("prd", "https://mapfile5.midea.com:6201");
        put("prd-red", "https://mapfilered5.midea.com:6201");
    }};

    public static int DEFAULT_MAX_CHANNEL_COUNT = 200;

    /**
     * 文件发送 FileSocketClient
     */
    private static volatile FileSocketClient fileClient;
    /**
     * 文件接收 FileSocketClient
     */
    private static volatile FileSocketClient fileClientRec;

    public static final Bucket DEFAULT_IM_BUCKET = new Bucket("601901", "366756", "8fcf46ac0b29");
    public static final Bucket RED_IM_BUCKET = new Bucket("657581", "366756", "8fcf46ac0b29");

    /**
     * 文件下载中的后缀
     */
    public static final String DOWNLOAD_TEMP_SUFFIX = ".download.";

    @SneakyThrows
    public static void initEnv(String env) {
        if (HOST == null) {
            synchronized (FileSDK.class) {
                if (HOST == null) {
                    String uri = uriMap.get(env);
                    log.info("当前使用uri为：{}", uri);
                    if (uri == null) {
                        throw new RuntimeException("环境配置错误");
                    }
                    URI fileUri = new URI(uri);
                    HOST = fileUri.getHost();
                    PORT = fileUri.getPort();

                    envBucket = env.contains("red") ? RED_IM_BUCKET : DEFAULT_IM_BUCKET;
                }
            }
        }
    }

    public static FileSocketClient getFileClient() {

        FileSDK.initEnv("sit");

        if (fileClient == null || fileClient.isClosed()) {
            synchronized (FileSDK.class) {
                if (fileClient == null || fileClient.isClosed()) {
                    fileClient = new FileSocketClient("sendClient", HOST, PORT, DEFAULT_MAX_CHANNEL_COUNT);
                }
            }
        }
        return fileClient;
    }

    public static FileSocketClient getFileClientRec() {

        FileSDK.initEnv("sit");

        if (fileClientRec == null || fileClientRec.isClosed()) {
            synchronized (FileSDK.class) {
                if (fileClientRec == null || fileClientRec.isClosed()) {
                    fileClientRec = new FileSocketClient("recClient", HOST, PORT, DEFAULT_MAX_CHANNEL_COUNT);
                }
            }
        }
        return fileClientRec;
    }

    public static String getUsername() {
        return username;
    }

    public static Bucket getBucket() {

        FileSDK.initEnv("sit");

        return envBucket;
    }

/**
 * 临时下载路径
 */
    public static String getDownloadTempPath(FileTaskInfoV5 fileTaskInfo) {
        String filePath = fileTaskInfo.getFilePath();
        String dir = filePath.substring(0, filePath.lastIndexOf("/") + 1);
        File directory = new File(dir);
        if (!directory.exists()) {
            boolean result = directory.mkdirs();
        }
        DownloadRequestV5 request = (DownloadRequestV5) fileTaskInfo.getRequest();
        return dir + DOWNLOAD_TEMP_SUFFIX + fileTaskInfo.getMd5() + "." + request.getTraceId();
    }

    public static boolean isDone(FileTaskInfoV5 fileTaskInfo) {
        long lastLength = fileTaskInfo.getFileSize() - fileTaskInfo.getOffset();
        int dataSize = (int) Math.min(lastLength, fileTaskInfo.getDataSize());
        return dataSize == 0;
    }
}
