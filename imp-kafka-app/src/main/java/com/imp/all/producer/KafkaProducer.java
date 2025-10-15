package com.imp.all.producer;

import com.imp.all.framework.common.pojo.CommonResult;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @author Longlin
 * @date 2021/6/3 10:54
 * @description
 */
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;

    public CommonResult<?> send(String topic, String message) {
        //发送带有时间戳的消息
        kafkaTemplate.send(topic, 0, System.currentTimeMillis(), "key", message);

        //使用ProducerRecord发送消息
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, 0, "ProducerRecordKey", message);
        kafkaTemplate.send(record);

        //使用Message发送消息
        Map<String, Object> map = new HashMap<>();
        map.put(KafkaHeaders.TOPIC, topic);
        map.put(KafkaHeaders.PARTITION_ID, 0);
        map.put(KafkaHeaders.MESSAGE_KEY, "GenericMessageKey");
        GenericMessage<String> genericMessage = new GenericMessage<>(message, new MessageHeaders(map));
        kafkaTemplate.send(genericMessage);

        return CommonResult.success();
    }

    public CommonResult<?> syncSend(String topic, String message) {
        try {
            kafkaTemplate.send(topic, message).get();
            return CommonResult.success();
        } catch (InterruptedException | ExecutionException e) {
            return CommonResult.failed(e.getMessage());
        }
    }
}
