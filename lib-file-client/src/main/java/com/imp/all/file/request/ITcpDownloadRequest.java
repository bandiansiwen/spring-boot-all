package com.imp.all.file.request;

/**
 * tcp 下载请求
 */
public interface ITcpDownloadRequest extends IMFileRequest{

    /**
     * 获取监听器
     *
     * @return
     */
//    List<IDownloadListener> getListeners();

    /**
     * 是否为缩略图请求
     * @return
     */
    boolean isThumRequest();

}
