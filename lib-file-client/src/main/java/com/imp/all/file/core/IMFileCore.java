package com.imp.all.file.core;

import com.imp.all.file.model.FileTaskInfoV5;
import com.imp.all.file.request.DownloadRequestV5;
import com.imp.all.file.request.UploadRequestV5;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Longlin
 * @date 2024/2/17 16:12
 * @description
 */
public class IMFileCore {

    static final String LOG_PRE_UPLOAD_SEND_FILE_INFO = ">>>> send (文件上传请求) cmd(51 0x33):";
    public static final String LOG_PRE_UPLOAD_REC_TASK_ID =
            "<<<< rec (上传,获取文件信息) cmd(52 0x34):";
    static final String LOG_PRE_UPLOAD_SEND_FILE_DATA =
            ">>>> send (上传,文件数据响应) cmd(54 0x36):";
    public static final String LOG_PRE_UPLOAD_REC_PULL_DATA =
            "<<<< rec (上传,文件数据请求) cmd(53 0x35):";

    public static final String LOG_PRE_DONE = "<<<< rec (成功) cmd(58 0x3A):";

    public static final String LOG_PRE_UPLOAD_REC_FILE_REQ =
            ">>>> send (文件信息请求) cmd(55 0x37):";
    public static final String LOG_PRE_UPLOAD_REC_FILE_RSP =
            "<<<< rec (文件信息响应) cmd(56 0x38):";
    public static final String LOG_PRE_DOWN_PULL_DATA_REQ =
            ">>>> send (下载,文件数据请求) cmd(53 0x35):";
    public static final String LOG_PRE_DOWN_PULL_DATA_RSP =
            "<<<< rec (下载,文件数据响应) cmd(54 0x36):";

    public static final String LOG_PRE_COPY_REQ = ">>>> send (文件拷贝请求) cmd(59 0x3B):";
    public static final String LOG_PRE_COPY_RSP = "<<<< rec (文件拷贝响应) cmd(60 0x3C):";

    public static final String LOG_UPDATE_STATE_DATA_REQ = "<<<< send 修改状态 cmd(37 0x39):";

    public static int getAccessTime() {
        return (int) (System.currentTimeMillis() / 1000);
    }

    private static final Map<String, CompletableFuture<Throwable>> uploadFutureMap = new ConcurrentHashMap<>();
    private static final Map<String, CompletableFuture<Throwable>> downloadFutureMap = new ConcurrentHashMap<>();

    public static boolean isImage(String filePath) {
        return filePath.endsWith(".png")
                || filePath.endsWith(".jpg")
                || filePath.endsWith(".jpeg");
    }

    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if(dotIndex >= 0) { // 如果找到了"."
            return fileName.substring(dotIndex + 1);
        } else { // 如果没有找到"."
            return "";
        }
    }

    public static CompletableFuture<Throwable> onUploadStart(String requestId, FileTaskInfoV5 fileTaskInfoV5) {
        CompletableFuture<Throwable> future = new CompletableFuture<>();
        uploadFutureMap.put(requestId, future);
        IMFileTaskQueueV5.putUploadTask(requestId, fileTaskInfoV5);
        IMFileSenderV5.upload((UploadRequestV5) fileTaskInfoV5.getRequest());
        return future;
    }

    public static void onUploadSuccess(String requestId) {
        IMFileTaskQueueV5.removeUploadTask(requestId);
        CompletableFuture<Throwable> future = uploadFutureMap.get(requestId);
        future.complete(null);
        uploadFutureMap.remove(requestId);
    }

    public static void onUploadError(String requestId, Throwable exception) {
        IMFileTaskQueueV5.removeUploadTask(requestId);
        CompletableFuture<Throwable> future = uploadFutureMap.get(requestId);
        future.complete(exception);
        uploadFutureMap.remove(requestId);
    }

    public static CompletableFuture<Throwable> onDownloadStart(String requestId, FileTaskInfoV5 fileTaskInfoV5) {
        CompletableFuture<Throwable> future = new CompletableFuture<>();
        downloadFutureMap.put(requestId, future);
        IMFileTaskQueueV5.putDownloadTask(requestId, fileTaskInfoV5);
        IMFileSenderV5.download((DownloadRequestV5) fileTaskInfoV5.getRequest());
        return future;
    }

    public static void onDownloadSuccess(String requestId) {
        IMFileTaskQueueV5.removeDownloadTask(requestId);
        CompletableFuture<Throwable> future = downloadFutureMap.get(requestId);
        future.complete(null);
        downloadFutureMap.remove(requestId);
    }

    public static void onDownloadError(String requestId, Throwable exception) {
        IMFileTaskQueueV5.removeDownloadTask(requestId);
        CompletableFuture<Throwable> future = downloadFutureMap.get(requestId);
        future.complete(exception);
        downloadFutureMap.remove(requestId);
    }
}
