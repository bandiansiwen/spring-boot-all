package com.imp.all.file.core;

import com.imp.all.file.model.FileTaskInfoV5;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Longlin
 * @date 2024/2/17 16:45
 * @description
 */
public class IMFileTaskQueueV5 {

    private static final Map<String, FileTaskInfoV5> uploadIngTaskMap = new ConcurrentHashMap<>();
    private static final Map<String, FileTaskInfoV5> downIngTaskMap = new ConcurrentHashMap<>();

    public static synchronized void putUploadTask(String requestId, FileTaskInfoV5 fileTaskInfoV5) {
        uploadIngTaskMap.put(requestId, fileTaskInfoV5);
    }
    public static synchronized FileTaskInfoV5 getUploadTask(String requestId) {
        return uploadIngTaskMap.get(requestId);
    }
    public static synchronized void removeUploadTask(String requestId) {
        uploadIngTaskMap.remove(requestId);
    }

    public static synchronized FileTaskInfoV5 fetchUploadTaskById(String requestId) {
        for (FileTaskInfoV5 taskInfo : uploadIngTaskMap.values()) {
            if (requestId.equals(taskInfo.getRequestId())) {
                return taskInfo;
            }
        }
        return null;
    }

    public static synchronized void putDownloadTask(String requestId, FileTaskInfoV5 fileTaskInfoV5) {
        downIngTaskMap.put(requestId, fileTaskInfoV5);
    }
    public static synchronized FileTaskInfoV5 getDownloadTask(String requestId) {
        return downIngTaskMap.get(requestId);
    }
    public static synchronized void removeDownloadTask(String requestId) {
        downIngTaskMap.remove(requestId);
    }
}
