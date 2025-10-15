package com.imp.all.file.type;

/**
 */
public enum FileCmdType {

    //<editor-fold desc="文件4">
    /**
     * 上传/发送文件请求(在线/离线)
     */
    IMSendFileReq(1),
    /**
     * 上传/发送文件请求响应(在线/离线)
     */
    IMSendFileRsp(2),
    /**
     * 下载/接收文件请求(在线/离线)
     */
    IMReceiveFileReq(3),
    /**
     * 下载/接收文件请求响应(在线/离线)
     */
    IMReceiveFileRsp(4),
    /**
     * 文件数据请求
     */
    IMFilePullDataReq(5),
    /**
     * 文件数据请求
     */
    IMFilePullDataRsp(6),
    /**
     * 状态通知
     */
    IMFileState(7),
    /**
     * 状态通知响应
     */
    IMFileStateRsp(8),
    //</editor-fold>

    //<editor-fold desc="文件5">
    /**
     * 上传/发送文件请求(在线/离线)
     */
    FILE_UPLOAD_REQUEST(51),
    /**
     * 上传/发送文件请求响应(在线/离线)
     */
    FILE_UPLOAD_RESPONSE(52),
    /**
     * 文件数据请求
     */
    FILE_PULLDATA_REQUEST(53),
    /**
     * 文件数据请求
     */
    FILE_PULLDATA_RESPONSE(54),
    /**
     * 下载/接收文件请求(在线/离线)
     */
    FILE_GETINFO_REQUEST(55),
    /**
     * 下载/接收文件请求响应(在线/离线)
     */
    FILE_GETINFO_RESPONSE(56),
    /**
     * 状态通知
     */
    FILE_UPDATESTATUS_REQUEST(57),
    /**
     * 状态通知响应
     */
    FILE_UPDATESTATUS_RESPONE(58),
    /**
     * 拷贝 filekey
     */
    FILE_COPY_REQUEST(59),
    /**
     * 拷贝 filekey 响应
     */
    FILE_COPY_RESPONE(60),
    //</editor-fold>

    ;

    int cmd;

    FileCmdType(int cmd) {
        this.cmd = cmd;
    }

    public static FileCmdType valueOf(int cmd) {
        for (FileCmdType type : FileCmdType.values()) {
            if (type.cmd == cmd)
                return type;
        }
        return null;
    }

    public byte value() {
        return (byte) cmd;
    }

    public boolean isV5() {
        return cmd > 50;
    }

    public boolean isDownloadCmd() {
        return this == FILE_GETINFO_REQUEST
                || this == FILE_PULLDATA_REQUEST
                || this == IMReceiveFileReq
                || this == IMFilePullDataReq;
    }

    public boolean isUploadCmd() {
        return this == FILE_UPLOAD_REQUEST
                || this == IMSendFileReq;
    }


}
