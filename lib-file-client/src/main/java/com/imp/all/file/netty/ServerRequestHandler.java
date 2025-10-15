package com.imp.all.file.netty;

import com.imp.all.file.core.IMFileResponseHandlerV5;
import com.imp.all.file.pb.IMFileV5;
import com.imp.all.file.type.FileCmdType;
import com.imp.all.file.type.TransmissionType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Longlin
 * @date 2024/2/3 15:40
 * @description
 */
@Slf4j
public class ServerRequestHandler extends SimpleChannelInboundHandler<FileResponse>  {
    private String name;

    private boolean sendHeartBeat = false;

    public ServerRequestHandler(String name) {
        this.name = name;
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        boolean match = super.acceptInboundMessage(msg);
        if (match) {
            if (msg instanceof FileResponse) {
                FileResponse fileResponse = (FileResponse) msg;
                if (fileResponse.getType() == TransmissionType.PB) {
                    FileCmdType cmd = fileResponse.getCmd();
                    log.info(name + " rec server sq:" + fileResponse.getSq() + " ,cmd:" + cmd);
                    boolean enableHeartbeat;
                    if (sendHeartBeat) {
                        //收到服务器更新文件状态请求时停止回复心跳
                        enableHeartbeat = cmd != FileCmdType.FILE_UPDATESTATUS_REQUEST;
                    } else {
                        //收到服务器请求上传数据的请求时开始回复心跳
                        enableHeartbeat = cmd == FileCmdType.FILE_PULLDATA_REQUEST;
                    }
                    if (sendHeartBeat != enableHeartbeat) {
                        if (enableHeartbeat) {
                            log.info(name + " enable HeartBeat");
                        } else {
                            log.info(name + " disable HeartBeat");
                        }
                    }
                    sendHeartBeat = enableHeartbeat;

                    if (fileResponse.getCmd() == FileCmdType.FILE_PULLDATA_REQUEST
                            || fileResponse.getCmd() == FileCmdType.FILE_UPDATESTATUS_REQUEST) {
                        return true;
                    } else if (fileResponse.getCmd() == FileCmdType.FILE_UPLOAD_RESPONSE) {
                        IMFileV5.FileUploadRsp rsp = IMFileV5.FileUploadRsp.parseFrom(fileResponse.getPbData());
                        return false;
                    }
                } else if (fileResponse.getType() == TransmissionType.HEARTBEAT_REQ) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FileResponse msg) throws Exception {
        log.info("{} receive server request:{},length:${}", name, msg.getType().name(), msg.getData().length);
        if (msg.getType() == TransmissionType.HEARTBEAT_REQ) {
            if (sendHeartBeat && msg.getType() == TransmissionType.HEARTBEAT_REQ) {
                log.info("send HeartBeat");
                ctx.writeAndFlush(assembleHeartbeat());
            }
        } else {
            try {
                handleMsg(ctx, msg);
            } catch (Throwable e) {
                exceptionCaught(ctx, e);
            }
        }
    }

    private void handleMsg(ChannelHandlerContext ctx, FileResponse msg) throws Exception {
        byte[] data = msg.getPbData();
        if (msg.getCmd() == FileCmdType.FILE_PULLDATA_REQUEST) {
            IMFileV5.FilePullDataReq pullDataReq = IMFileV5.FilePullDataReq.parseFrom(data);
            log.info("sq:" + msg.getSq() + ", cmd:" + msg.getCmd() + ", fileKey:" + pullDataReq.getFileKey());
            IMFileResponseHandlerV5.handlePullDataReq(msg.getSq(), pullDataReq);
        } else if (msg.getCmd() == FileCmdType.FILE_UPDATESTATUS_REQUEST) {
            IMFileV5.FileUpdateStatusReq updateStatusReq = IMFileV5.FileUpdateStatusReq.parseFrom(data);
            log.info("sq:" + msg.getSq() + ", cmd:" + msg.getCmd() + ", fileKey:" + updateStatusReq.getFileKey());
            IMFileResponseHandlerV5.handleDone(msg.getSq(), updateStatusReq);
        }
    }

    private byte[] assembleHeartbeat() {
        byte[] requestByte = new byte[7];
        requestByte[0] = TransmissionType.HEARTBEAT_RSP.getByte();
        requestByte[1] = 0;
        requestByte[2] = 0;
        requestByte[3] = 0;
        requestByte[4] = 0;
        requestByte[5] = -1;
        requestByte[6] = -1;
        return requestByte;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("$name channel:${} inactive", ctx.channel().id());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
