package com.imp.all.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.List;
import java.util.Optional;

/**
 * @author Longlin
 * @date 2021/6/3 10:59
 * @description
 */
@Slf4j
public class KafkaConsumer {

    // 单个消费
    // 默认注入 kafkaListenerContainerFactory
    @KafkaListener(topics = "aaaTopic", errorHandler = "consumerAwareErrorHandler")
    public void processMessage(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("消费 aaaTopic 消息：record = {}, ", record);
            log.info("message =" + message);
//            throw new RuntimeException("自己出错");
        }
        //手动提交
        ack.acknowledge();
    }

    // 批量消息
    @KafkaListener(topics = {"bbbTopic"}, containerFactory = "batchContainerFactory")
    public void consumerBatch(List<ConsumerRecord<?, ?>> recordList, Acknowledgment ack) {
        log.info("接收到消息数量：{}", recordList.size());
        for (ConsumerRecord<?, ?> record : recordList) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                Object message = kafkaMessage.get();
                log.info("消费 bbbTopic 消息：message = {}", message);
            }
        }
        //手动提交
        ack.acknowledge();
    }

    // 批量消息
    @KafkaListener(
            groupId = "cListener",
            containerFactory = "filterContainerFactory",
            topicPartitions = {@TopicPartition(topic = "cccTopic", partitions = {"0"})}
    )
    public void listenPartition0(List<ConsumerRecord<?, ?>> recordList, Acknowledgment ack) {
        log.info("listenPartition0 接收到消息数量：{}", recordList.size());
        for (ConsumerRecord<?, ?> record : recordList) {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            if (kafkaMessage.isPresent()) {
                Object message = kafkaMessage.get();
                log.info("消费 cccTopic 消息：message = {}", message);
            }
        }
        //手动提交
        ack.acknowledge();
    }

    // 注解方式获取消息头及消息体
    @KafkaListener(topics = "dddTopic")
    public void processMessage2(@Payload String data,
                                @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                @Header(KafkaHeaders.RECEIVED_PARTITION_ID) String partition,
                                @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                @Header(KafkaHeaders.RECEIVED_TIMESTAMP) String ts) {
        log.info(" receive : \n" +
                "data : " + data + "\n" +
                "key : " + key + "\n" +
                "partitionId : " + partition + "\n" +
                "topic : " + topic + "\n" +
                "timestamp : " + ts + "\n"
        );
    }
}
