package com.imp.all.file.netty;

import com.google.protobuf.InvalidProtocolBufferException;
import com.imp.all.file.error.FileErrorMap;
import com.imp.all.file.error.ImFileException;
import com.imp.all.file.policy.RetryPolicy;
import com.imp.all.file.type.FileCmdType;
import com.imp.all.file.type.TransmissionType;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Longlin
 * @date 2024/2/4 15:32
 * @description
 */
@Slf4j
public abstract class ClientRequestWrapper extends SimpleChannelInboundHandler<FileResponse> {
    @Getter
    @Setter
    private FileSocketClient client;
    @Getter
    @Setter
    private int sq;
    @Getter
    @Setter
    private FileCmdType cmd;
    @Getter
    @Setter
    private String fileKey;
    @Getter
    @Setter
    private String traceId;
    @Getter
    @Setter
    private Assemble assemble;
    @Getter
    @Setter
    private boolean enableRetry;

    private ChannelHandlerContext ctx;
    private AtomicInteger retryCount = new AtomicInteger();
    private FileRequest request;
    private ScheduledFuture<?> timeoutFuture;
    private ScheduledFuture<?> retryFuture;

    public ClientRequestWrapper(FileSocketClient client, int sq, FileCmdType cmd, String fileKey, Assemble assemble, boolean enableRetry) {
        this.client = client;
        this.sq = sq;
        this.cmd = cmd;
        this.fileKey = fileKey;
        this.assemble = assemble;
        this.enableRetry = enableRetry;
    }

    @Override
    public boolean isSharable() {
        return true;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        if (request != null) {
            ctx.writeAndFlush(request).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("sq:" + sq + " cmd:" + cmd + " sent success");
                    } else {
                        log.error("sq:" + sq + " cmd:" + cmd + " sent failed");
                    }
                }
            });
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        this.ctx = ctx;
        if (enableRetry) {
            // Timeout timer
            timeoutFuture = ctx.executor().schedule(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    int errorCode;
                    if (cmd.isDownloadCmd()) {
                        errorCode = FileErrorMap.ERROR_CODE_DOWNLOAD_TIMEOUT;
                    } else if (cmd.isUploadCmd()) {
                        errorCode = FileErrorMap.ERROR_CODE_UPLOAD_TIMEOUT;
                    } else {
                        errorCode = FileErrorMap.ERROR_CODE_DOWNLOAD_COPY_FAILED;
                    }
                    exceptionCaught(ctx, new ImFileException(sq, cmd, errorCode));
                }
            }, RetryPolicy.getCallTimeout(), TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        this.ctx = null;
    }

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg)
                && msg instanceof FileResponse
                && ((FileResponse) msg).getType() == TransmissionType.PB
                && ((FileResponse) msg).getSq() == sq;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FileResponse msg) throws Exception {
        if (timeoutFuture!=null) {
            timeoutFuture.cancel(true);
        }
        if (!checkIfCancel()) {
            try {
                 FileCmdType cmd = FileCmdType.valueOf(msg.getData()[2]);
                byte[] dataPB = Arrays.copyOfRange(msg.getData(), 3, msg.getData().length);
                onSuccess(sq, cmd, fileKey, dataPB);
            } catch (Throwable e) {
                retry(ctx.executor(), e);
            }
        }
        ctx.pipeline().remove(this);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        retry(ctx.executor(), cause);
        ctx.pipeline().remove(this);
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        log.info("channel:${} inactive", ctx.channel().id());
    }

    public void retry(EventExecutor executor, Throwable cause) {
        cause.printStackTrace();

        if (checkIfCancel()) {
            if (retryFuture != null) {
                retryFuture.cancel(true);
            }
            return;
        }
        if (retryCount.get() < RetryPolicy.getMaxTryCount()) {
            if (retryFuture != null) {
                retryFuture.cancel(true);
            }
            retryFuture = executor.schedule(new Runnable() {
                @Override
                public void run() {
                    log.info("request sq:" + sq + " cmd:" + cmd + " fileKey:" + fileKey + " retry");
                    retryCount.incrementAndGet();
                    request();
                }
            }, RetryPolicy.getRetryDelay(), TimeUnit.MILLISECONDS);
        } else {
            // Exceeded retry count onError
            onError(sq, cmd, fileKey, traceId, cause);
        }
    }

    public ClientRequestWrapper request() {
        if (checkIfCancel()) {
            log.info("request sq:" + sq + " cmd:" + cmd + " fileKey:" + fileKey + " has been canceled");
            return this;
        }
        try {
            request = assemble.run(sq, retryCount.get());
            client.sendRequest(request, this);
        } catch (Throwable e) {
            onError(sq, cmd, fileKey, traceId, e);
        }
        return this;
    }

    public abstract void onSuccess(int sq, FileCmdType cmd, String requestFileKey, byte[] data) throws InvalidProtocolBufferException;

    public abstract void onError(int sq, FileCmdType cmd, String requestFileKey, String traceId, Throwable e);

    public abstract boolean checkIfCancel();

    public void cancel() {
        timeoutFuture.cancel(true);
        retryFuture.cancel(true);
        ctx.pipeline().remove(this);
    }

    public interface Assemble {
        FileRequest run(int sq, int retryCount) throws Throwable;
    }
}
