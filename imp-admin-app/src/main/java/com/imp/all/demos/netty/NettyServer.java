package com.imp.all.demos.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Slf4j
public class NettyServer {
    /**
     * 端口号
     */
    @Value("${webSocket.netty.port}")
    private int port;

    /**
     * webSocket路径
     */
    @Value("${webSocket.netty.path}")
    private String webSocketPath;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public void run() throws Exception {
        //主线程组,接收请求
        bossGroup = new NioEventLoopGroup();
        //创建从线程组，处理主线程组分配下来的io操作
        workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                    .group(bossGroup, workGroup)  //设置主从线程组
                    .channel(NioServerSocketChannel.class)  //设置通道
                    .childHandler(new NettyServerInitializer(webSocketPath)) //子处理器，用于处理workerGroup中的操作
                    //设置队列大小
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 两小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            //绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(port).sync();
            log.info("netty 服务器启动开始监听端口: {}", port);
            //监听关闭channel
            future.channel().closeFuture().sync();
        } finally {
            //关闭主线程组
            bossGroup.shutdownGracefully();
            //关闭工作线程组
            workGroup.shutdownGracefully();
            log.info("NettyServer 关闭了");
        }
    }

    @PostConstruct()
    public void init() {
        //让Netty的启动，与我们程序的启动不能再同一个线程内启动。
        new Thread(() -> {
            try {
                run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @PreDestroy
    public void destroy() throws InterruptedException {
        if(bossGroup != null){
            bossGroup.shutdownGracefully().sync();
        }
        if(workGroup != null){
            workGroup.shutdownGracefully().sync();
        }
    }
}
