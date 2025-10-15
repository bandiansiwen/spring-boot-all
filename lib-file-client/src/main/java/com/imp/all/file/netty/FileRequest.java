package com.imp.all.file.netty;

import com.google.protobuf.Message;
import com.imp.all.file.type.FileCmdType;
import com.imp.all.file.utils.MsgUtil;

/**
 * @author Longlin
 * @date 2024/2/3 15:23
 * @description
 */
public class FileRequest {

    private Integer sq;
    private FileCmdType type;
    private Message.Builder message;

    public FileRequest(int sq, FileCmdType type, Message.Builder message) {
        this.sq = sq;
        this.type = type;
        this.message = message;
    }

    public byte[] encode() {
        return MsgUtil.assembleFileRequestBytes(sq, type, message);
    }

}
