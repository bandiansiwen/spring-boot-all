package com.imp.all.file.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.FixedChannelPool;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * @author Longlin
 * @date 2024/2/1 15:52
 * @description
 */
@Slf4j
public class FileSocketClient {

    private static final String WORKER_THREAD_NAME_PREFIX = "file-netty-event-loop";
    private String name;
    private NioEventLoopGroup mEventLoopsGroup;
    private Bootstrap mBootstrap;
    private FixedChannelPool mChannelPool;
    private volatile boolean mIsClosed = false;
    public final static int READER_IDLE_TIME_SECONDS = 60; //读操作空闲
    public final static int WRITER_IDLE_TIME_SECONDS = 60;//写操作空闲
    public final static int ALL_IDLE_TIME_SECONDS = 60;//读写全部空闲
    private static final int CONNECT_TIME_OUT = 7 * 1000; //连接超时时间

    public FileSocketClient(String name, String host, int port, int maxChannelCount) {

        this.name = name;
        this.mEventLoopsGroup = new NioEventLoopGroup(new DefaultThreadFactory(WORKER_THREAD_NAME_PREFIX, Thread.MAX_PRIORITY));
        this.mBootstrap = new Bootstrap()
                .group(mEventLoopsGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIME_OUT)
                .remoteAddress(new InetSocketAddress(host, port));

        this.mChannelPool = new FixedChannelPool(mBootstrap, new ChannelPoolHandler() {
            @Override
            public void channelReleased(Channel ch) {
                log.info(name + " channel:" + ch.id() + " released");
            }

            @Override
            public void channelAcquired(Channel ch) {
                log.info(name + " channel:" + ch.id() + " acquired");
            }

            @Override
            public void channelCreated(Channel ch) {
                log.info(name + " channel:" + ch.id() + " init");
                ch.pipeline()
                        .addLast(new IdleStateHandler(READER_IDLE_TIME_SECONDS, WRITER_IDLE_TIME_SECONDS, ALL_IDLE_TIME_SECONDS))
                        .addLast(new ImFileResponseDecoder())
                        .addLast(new ByteArrayEncoder())
                        .addLast(new ImFileRequestEncoder())
                        .addLast(new ServerRequestHandler(name));
            }
        }, maxChannelCount);
    }

    public void sendRequest(FileRequest request, ClientRequestWrapper callback) {
        if (mIsClosed) {
            throw new IllegalStateException("Can not request after shutdown");
        }
        mChannelPool.acquire().addListener(new FutureListener<Channel>() {
            @Override
            public void operationComplete(Future<Channel> future) {
                if (future.isSuccess()) {
                    Channel channel = future.getNow();
                    if (channel != null) {
                        if (callback != null) {
                            channel.pipeline().addLast(callback);
                        }
                        channel.writeAndFlush(request).addListener(future1 -> {
                            if (future1.isSuccess()) {
                                log.info("sent success");
//                                log.info("sq:" + callback.getSq() + " cmd:" + callback.getCmd() + " sent success");
                            } else {
                                log.info("sent failed");
//                                log.info("sq:" + callback.getSq() + " cmd:" + callback.getCmd() + " sent failed");
                            }
                            mChannelPool.release(channel);
                        });
                    }
                } else {
                    if (callback != null) {
                        callback.retry(mEventLoopsGroup.next(), future.cause());
                    }
                }
            }
        });
    }

    public void shutdown() {
        if (!mIsClosed) {
            mChannelPool.close();
            mEventLoopsGroup.shutdownGracefully();
            mIsClosed = true;
        }
    }

    public boolean isClosed() {
        return mIsClosed;
    }
}
