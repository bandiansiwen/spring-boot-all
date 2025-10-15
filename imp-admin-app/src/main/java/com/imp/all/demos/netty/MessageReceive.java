package com.imp.all.demos.netty;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MessageReceive {

    /**
     * 订阅消息,发送给指定用户
     * @param object
     */
    public void getMessageToOne(String object) {
        Jackson2JsonRedisSerializer serializer = getSerializer(NettyPushMessageBody.class);
        NettyPushMessageBody pushMessageBody = (NettyPushMessageBody) serializer.deserialize(object.getBytes());
        log.info("订阅消息,发送给指定用户：" + Objects.requireNonNull(pushMessageBody).toString());

        String message = pushMessageBody.getMessage();
        String userId = pushMessageBody.getUid();
        ConcurrentHashMap<String, Channel> userChannelMap = ChannelHandlerPool.getUserChannelMap();
        Channel channel = userChannelMap.get(userId);
        Objects.requireNonNull(channel).writeAndFlush(new TextWebSocketFrame(message));
    }

    /**
     * 订阅消息，发送给所有用户
     * @param object
     */
    public void getMessageToAll(String object) {
        Jackson2JsonRedisSerializer serializer = getSerializer(String.class);
        String message = (String) serializer.deserialize(object.getBytes());
        log.info("订阅消息，发送给所有用户：" + message);
        ChannelHandlerPool.getChannelGroup().writeAndFlush(new TextWebSocketFrame(message));
    }

    private Jackson2JsonRedisSerializer getSerializer(Class clazz) {
        // 序列化对象
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer(clazz);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }
}
