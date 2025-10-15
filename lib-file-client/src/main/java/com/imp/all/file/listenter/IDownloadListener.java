package com.imp.all.file.listenter;

import com.imp.all.file.model.IMFileTask;
import com.imp.all.file.request.IMFileRequest;
import lombok.NonNull;

/**
 * 文件下监听接口
 *
 * @author liangyj3
 */
public abstract class IDownloadListener extends CancelAbleListener {

    /**
     * 文件下载开始回调
     *
     * @param requestId 文件下载请求Id
     * @param request   IMFileRequest请求对象
     */
    public abstract void onStart(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo);

    /**
     * 文件下载错误回调
     *
     * @param requestId 文件下载请求Id
     * @param request   IMFileRequest请求对象
     * @param throwable Throwable异常
     */
    public abstract void onError(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull Throwable throwable);

    /**
     * 文件下载取消回调
     *
     * @param requestId 文件下载请求Id
     * @param request   IMFileRequest请求对象
     */
    public abstract void onCancel(@NonNull String requestId, @NonNull IMFileRequest request);

    /**
     * 文件下载成功回调
     *
     * @param requestId    文件下载请求Id
     * @param request      IMFileRequest请求对象
     * @param fileTaskInfo IMFileTask对象
     */
    public abstract void onSuccess(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo);

    /**
     * 文件下载过程回调
     *
     * @param requestId    文件下载请求Id
     * @param request      IMFileRequest请求对象
     * @param fileTaskInfo IMFileTask对象
     */
    public abstract void onProcess(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo);
}
