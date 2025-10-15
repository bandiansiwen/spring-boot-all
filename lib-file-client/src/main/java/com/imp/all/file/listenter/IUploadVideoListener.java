package com.imp.all.file.listenter;

import com.imp.all.file.model.IMFileTask;
import com.imp.all.file.request.IMFileRequest;
import com.imp.all.file.request.UploadRequestV5;
import lombok.NonNull;

/**
 * 视频上传监听器
 *
 * @author liangyj3
 */
public abstract class IUploadVideoListener extends IUploadListener {

    boolean isCoverDone = false;
    boolean isVideoDone = false;
    private IMFileTask coverTask;
    private IMFileTask videoTask;
    private IMFileRequest request;
    private boolean hasCallDone = false;

    /**
     * 视频上传开始回调（重写）
     *
     * @param requestId 视频文件请求Id
     * @param request   IMFileRequest文件对象
     */
    @Override
    public void onStart(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo) {
        onStart(isCover(request), requestId, request);
    }

    /**
     * 视频上传错误回调（重写）
     *
     * @param requestId 视频文件请求Id
     * @param request   IMFileRequest文件对象
     * @param throwable Throwable异常
     */
    @Override
    public void onError(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull Throwable throwable) {
        onError(isCover(request), requestId, request, throwable);
    }

    /**
     * 视频上传取消回调（重写）
     *
     * @param requestId 视频文件请求Id
     * @param request   IMFileRequest文件对象
     */
    @Override
    public void onCancel(@NonNull String requestId, @NonNull IMFileRequest request) {
        onCancel(isCover(request), requestId, request);
    }

    /**
     * 视频上传成功回调（重写）
     *
     * @param requestId    视频文件请求Id
     * @param request      IMFileRequest文件对象
     * @param fileTaskInfo IMFileTask对象
     */
    @Override
    public void onSuccess(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo) {
        boolean isCover = isCover(request);
        onSuccess(isCover, requestId, request, fileTaskInfo);

        if (isCover) {
            coverTask = fileTaskInfo;
            isCoverDone = true;
        } else {
            videoTask = fileTaskInfo;
            isVideoDone = true;
        }

        if (isCoverDone && isVideoDone) {
            if (!hasCallDone) {
                hasCallDone = true;
                onDone(request, coverTask, videoTask);
            }

        }
    }

    /**
     * 视频上传过程回调（重写）
     *
     * @param requestId    视频文件请求Id
     * @param request      IMFileRequest请求对象
     * @param fileTaskInfo IMFileTask对象
     */
    @Override
    public void onProcess(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo) {
        onProcess(isCover(request), requestId, request, fileTaskInfo.getOffset(), fileTaskInfo.getFileSize());
    }

    private boolean isCover(IMFileRequest request) {
        if (request instanceof UploadRequestV5) {
//            int videoPartKey = (int) ((UploadRequestV5) request).getTag(UploadRequestVideoV5Builder.TAG_VIDEO_PART_KEY);
//            return videoPartKey == UploadRequestVideoV5Builder.TAG_VIDEO_COVER;
        }
        return false;
    }

    /**
     * 视频上传开始
     *
     * @param isCover   是否覆盖
     * @param requestId 文件请求Id
     * @param request   IMFileRequest请求对象
     */
    public abstract void onStart(boolean isCover, String requestId, IMFileRequest request);

    /**
     * 视频上传错误
     *
     * @param isCover   是否覆盖
     * @param requestId 文件请求Id
     * @param request   IMFileRequest请求对象
     * @param throwable Throwable异常
     */
    public abstract void onError(boolean isCover, String requestId, IMFileRequest request, Throwable throwable);

    /**
     * 视频上传取消
     *
     * @param isCover   是否覆盖
     * @param requestId 文件请求Id
     * @param request   IMFileRequest请求对象
     */
    public abstract void onCancel(boolean isCover, String requestId, IMFileRequest request);

    /**
     * 视频上传成功
     *
     * @param isCover      是否覆盖
     * @param requestId    文件请求Id
     * @param request      IMFileRequest请求对象
     * @param fileTaskInfo IMFileTask对象
     */
    public abstract void onSuccess(boolean isCover, String requestId, IMFileRequest request, IMFileTask fileTaskInfo);

    /**
     * 视频上传处理过程
     *
     * @param isCover   是否覆盖
     * @param requestId 文件请求Id
     * @param request   IMFileRequest请求对象
     * @param offset    视频上传大小
     * @param fileSize  视频大小
     */
    public abstract void onProcess(boolean isCover, String requestId, IMFileRequest request, long offset, long fileSize);

    /**
     * 视频上传完成
     *
     * @param request   文件请求Id
     * @param coverTask IMFileTask对象
     * @param videoTask IMFileTask对象
     */
    public abstract void onDone(IMFileRequest request, IMFileTask coverTask, IMFileTask videoTask);
}
