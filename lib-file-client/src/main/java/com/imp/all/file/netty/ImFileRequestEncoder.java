package com.imp.all.file.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @author Longlin
 * @date 2024/2/3 15:19
 * @description
 */
public class ImFileRequestEncoder extends MessageToMessageEncoder<FileRequest> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, FileRequest msg, List<Object> list) throws Exception {
        list.add(Unpooled.wrappedBuffer(msg.encode()));
    }
}
