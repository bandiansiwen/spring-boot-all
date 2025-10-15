package com.imp.all.demos.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

    private final String webSocketPath;

    public NettyServerInitializer(String webSocketPath) {
        this.webSocketPath = webSocketPath;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // 流水线管理通道中的处理程序（Handler），用来处理业务
        // webSocket协议本身是基于http协议的，所以这边也要使用http编解码器
        pipeline.addLast(new HttpServerCodec());
        //支持写大数据流，以块的方式来写的处理器
        pipeline.addLast(new ChunkedWriteHandler());
        /*说明：http聚合器
         *1、http数据在传输过程中是分段的，HttpObjectAggregator可以将多个段聚合
         *2、这就是为什么，当浏览器发送大量数据时，就会发送多次http请求
         */
        pipeline.addLast(new HttpObjectAggregator(1024*64));
        //处理tcp粘包问题
        pipeline.addLast(new DelimiterBasedFrameDecoder(1024*8, Delimiters.lineDelimiter()));

        //针对客户端，若3s内无读事件则触发心跳处理方法HeartBeatHandler#userEventTriggered
        pipeline.addLast(new IdleStateHandler(5, 5, 10));
        //自定义空闲状态检测(自定义心跳检测handler)
        pipeline.addLast(new HeartBeatHandler());

        //websocket支持,设置路由
        pipeline.addLast(new WebSocketServerProtocolHandler(webSocketPath));

//        pipeline.addLast(new ObjectEncoder());
//        pipeline.addLast(new StringEncoder());//对 String 对象自动编码,属于出站站处理器
//        pipeline.addLast(new StringDecoder());//把网络字节流自动解码为 String 对象，属于入站处理器

        //添加自定义的助手类
        pipeline.addLast(new NettyServerHandler());
    }
}
