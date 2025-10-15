package com.imp.all.demos.netty;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.concurrent.ConcurrentHashMap;

public class ChannelHandlerPool {
    /**
     * 定义一个channel组，管理所有的channel
     * GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
     */
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    /**
     * 存放用户与Chanel的对应信息，用于给指定用户发送消息
     */
    private static final ConcurrentHashMap<String, Channel> userChannelMap = new ConcurrentHashMap<>();

    public ChannelHandlerPool() {}

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public static ConcurrentHashMap<String, Channel> getUserChannelMap() {
        return userChannelMap;
    }
}
