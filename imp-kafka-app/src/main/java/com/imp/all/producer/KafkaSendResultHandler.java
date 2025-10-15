package com.imp.all.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.ProducerListener;

/**
 * @author Longlin
 * @date 2021/6/10 14:04
 * @description 消息回调
 */
@Slf4j
public class KafkaSendResultHandler implements ProducerListener<Object, Object> {

    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("kafka 消息发送成功 : " + producerRecord.toString());
    }

    @Override
    public void onError(ProducerRecord producerRecord, Exception exception) {
        log.info("kafka 消息发送失败 : " + producerRecord.toString());
    }
}
