package com.imp.all.file.request;

/**
 * 请求接口
 */
public interface IMFileRequest {
    /**
     * v4 为 taskid
     * v5 为 fileKey
     *
     * @return
     */
    String getId();

    /**
     * 请求标签
     * @return
     */
    Object getTag();

    /**
     * 获取文件路径
     * @return
     */
    String getFilePath();

    /**
     * 传输完成时间戳
     * @return
     */
    long getDoneTimestamp();

    /**
     * 设置传输完成时间戳
     * @param doneTimestamp
     */
    void setDoneTimestamp(long doneTimestamp);
}
