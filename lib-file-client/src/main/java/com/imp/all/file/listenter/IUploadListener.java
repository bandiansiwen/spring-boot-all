package com.imp.all.file.listenter;

import com.imp.all.file.model.IMFileTask;
import com.imp.all.file.request.IMFileRequest;
import lombok.NonNull;

/**
 * 文件上传监听器
 *
 * @author liangyj3
 */
public abstract class IUploadListener extends CancelAbleListener {
    /**
     * 文件上传开始回调
     *
     * @param requestId 文件请求Id
     * @param request   IMFileRequest文件对象
     */
    public abstract void onStart(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo);

    /**
     * 文件上传错误回调
     *
     * @param requestId 文件请求Id
     * @param request   IMFileRequest文件对象
     * @param throwable Throwable异常
     */
    public abstract void onError(@NonNull String requestId, @NonNull IMFileRequest request, Throwable throwable);

    /**
     * 文件上传取消回调
     *
     * @param requestId 文件请求Id
     * @param request   IMFileRequest文件对象
     */
    public abstract void onCancel(@NonNull String requestId, @NonNull IMFileRequest request);

    /**
     * 文件上传成功回调
     *
     * @param requestId    文件请求Id
     * @param request      IMFileRequest文件对象
     * @param fileTaskInfo IMFileTask对象
     */
    public abstract void onSuccess(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo);

    /**
     * 文件上传过程回调
     *
     * @param requestId    文件请求Id
     * @param request      IMFileRequest请求对象
     * @param fileTaskInfo IMFileTask对象
     */
    public abstract void onProcess(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo);
}
