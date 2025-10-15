package com.imp.all.demos.netty;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import io.micrometer.core.instrument.util.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //TextWebSocketFrame是netty用于处理websocket发来的文本对象

    //接收到客户都发送的消息
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : ChannelHandlerPool.getChannelGroup()) {
            if (channel != incoming){
                channel.writeAndFlush(new TextWebSocketFrame("[" + incoming.remoteAddress() + "]" + msg.text()));
            }
//            else {
//                channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text() ));
//            }
        }
        log.info("服务器收到消息：{}",msg.text());

        // 获取用户名
        JSONObject jsonObject = JSONUtil.parseObj(msg.text());
        String uid = jsonObject.getStr("uid");
        if (StringUtils.isNotEmpty(uid)) {
            // 将用户名作为自定义属性加入到channel中，方便随时channel中获取用户名
            AttributeKey<String> key = AttributeKey.valueOf("uid");
            ctx.channel().attr(key).setIfAbsent(uid);

            // 关联channel
            ChannelHandlerPool.getUserChannelMap().put(uid,ctx.channel());
        }
        // 回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("{\"code\":202,\"msg\":\"successConnect阿萨\"}"));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : ChannelHandlerPool.getChannelGroup()) {
            // 通知其他人xx上线了
            channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 加入\n"));
        }
        ChannelHandlerPool.getChannelGroup().add(ctx.channel());
        log.info(ctx.channel().remoteAddress() + "上线了! 总人数:" + ChannelHandlerPool.getChannelGroup().size());
//        incoming.writeAndFlush("欢迎上线");
        incoming.writeAndFlush(new TextWebSocketFrame("欢迎上线"));
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : ChannelHandlerPool.getChannelGroup()) {
            channel.writeAndFlush(new TextWebSocketFrame("[SERVER] - " + incoming.remoteAddress() + " 离开\n"));
        }
        ChannelHandlerPool.getChannelGroup().remove(ctx.channel());
        removeUserId(ctx);
        log.info(ctx.channel().remoteAddress()+"断开连接! 总人数:" + ChannelHandlerPool.getChannelGroup().size());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Channel incoming = ctx.channel();
        log.info("SimpleChatClient:"+incoming.remoteAddress()+"在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Channel incoming = ctx.channel();
        removeUserId(ctx);
        log.info("SimpleChatClient:"+incoming.remoteAddress()+"掉线");
    }

    //出现异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel incoming = ctx.channel();
        removeUserId(ctx);
        log.info("SimpleChatClient:"+incoming.remoteAddress()+"异常");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 删除用户与channel的对应关系
     */
    private void removeUserId(ChannelHandlerContext ctx){
        AttributeKey<String> key = AttributeKey.valueOf("uid");
        String userName = ctx.channel().attr(key).get();
        if (StringUtils.isNotEmpty(userName)) {
            ChannelHandlerPool.getUserChannelMap().remove(userName);
        }
    }
}
