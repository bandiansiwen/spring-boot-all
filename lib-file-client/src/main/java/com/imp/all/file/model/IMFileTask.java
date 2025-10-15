package com.imp.all.file.model;

public interface IMFileTask {

    /**
     * 请求的文件fileKey
     *
     * @return
     */
    String getRequestId();

    /**
     * 请求缩略图时,realFileKey为服务器返回可用的fileKey
     *
     * @return
     */
    default String getRealFileKey() {
        return getRequestId();
    }

    long getOffset();

    long getFileSize();

    String getFilePath();

    /**
     * 仅 5.0 才提供
     *
     * @return
     */
    long getExpiredDay();

    float getProcess();
}
