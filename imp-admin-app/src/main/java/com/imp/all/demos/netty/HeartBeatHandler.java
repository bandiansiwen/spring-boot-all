package com.imp.all.demos.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    private int lossConnectCount = 0;

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent)evt;
            if (event.state() == IdleState.READER_IDLE){
                lossConnectCount ++;
                log.info(ctx.channel().remoteAddress() + "没有发送心跳包" + lossConnectCount + "次");
                // 三次自动断开
                if (lossConnectCount > 2){
                    ctx.channel().close();
                }
            }
            else if (event.state() == IdleState.WRITER_IDLE) {
                log.info("写超时");
            }
            else if (event.state() == IdleState.ALL_IDLE) {
                log.info("读写超时");
            }
        } else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
