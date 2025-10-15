package com.imp.all.demos.netty;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PushServiceImpl implements PushService{

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void pushMsgToOne(String uid, String msg) {
        ConcurrentHashMap<String, Channel> userChannelMap = ChannelHandlerPool.getUserChannelMap();
        Channel channel = userChannelMap.get(uid);
        if(!Objects.isNull(channel)){
            // 如果该用户的客户端是与本服务器建立的channel,直接推送消息
            channel.writeAndFlush(new TextWebSocketFrame(msg));
        }else {
            // 发布，给其他服务器消费
            NettyPushMessageBody pushMessageBody = new NettyPushMessageBody();
            pushMessageBody.setUid(uid);
            pushMessageBody.setMessage(msg);
            redisTemplate.convertAndSend(PushConstants.PUSH_MESSAGE_TO_ONE,pushMessageBody);
        }
    }

    @Override
    public void pushMsgToAll(String msg) {
        // 发布，给其他服务器消费
        redisTemplate.convertAndSend(PushConstants.PUSH_MESSAGE_TO_ALL,msg);
    }
}
