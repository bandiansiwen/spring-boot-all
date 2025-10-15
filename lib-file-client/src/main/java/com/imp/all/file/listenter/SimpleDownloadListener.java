package com.imp.all.file.listenter;

import com.imp.all.file.model.IMFileTask;
import com.imp.all.file.request.IMFileRequest;
import lombok.NonNull;

/**
 * @author Longlin
 * @date 2024/2/17 16:29
 * @description
 */
public class SimpleDownloadListener extends IDownloadListener {
    @Override
    public void onStart(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo) {

    }

    @Override
    public void onError(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull Throwable throwable) {

    }

    @Override
    public void onCancel(@NonNull String requestId, @NonNull IMFileRequest request) {

    }

    @Override
    public void onSuccess(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo) {

    }

    @Override
    public void onProcess(@NonNull String requestId, @NonNull IMFileRequest request, @NonNull IMFileTask fileTaskInfo) {

    }
}
