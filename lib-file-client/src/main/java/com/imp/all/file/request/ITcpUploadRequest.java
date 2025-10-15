package com.imp.all.file.request;

/**
 * 文件上传请求接口
 */
public interface ITcpUploadRequest extends IMFileRequest {

    /**
     * 获取上传监听器
     *
     * @return
     */
//    List<IUploadListener> getListeners();

    /**
     * 设置是否需要生成缩略图
     * @param needThum
     */
    void setNeedThum(boolean needThum);

}
