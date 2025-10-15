package com.imp.all.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Longlin
 * @date 2021/6/3 10:58
 * @description
 */
@Configuration
@Slf4j
@AutoConfigureAfter({KafkaProperties.class})
public class KafkaConsumerConfiguration {

    @Resource
    private KafkaProperties kafkaProperties;

    //获得创建消费者工厂
    @Bean
    public ConsumerFactory<Object, Object> consumerFactory() {
        Map<String, Object> consumerProperties = this.kafkaProperties.buildConsumerProperties();
        //对模板 consumerProperties 进行定制化
        //....
        //自动提交的频率
//        consumerProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitInterval);
//        //连接地址
//        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        //是否开启自动提交
//        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
//        //一次拉取消息数量
//        consumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
//        //连接超时时间
//        consumerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
//        //键反序列化方式
//        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        //值反序列化方式
//        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(consumerProperties);
    }

    // org.springframework.boot.autoconfigure.kafka.KafkaAnnotationDrivenConfiguration
    // 有一个默认配置的 ConcurrentKafkaListenerContainerFactory
    // 这里是自定义
    @Bean
    public KafkaListenerContainerFactory<?> batchContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<?, ?> factory = new ConcurrentKafkaListenerContainerFactory<>();
        // 并发数 多个微服务实例会均分
        factory.setConcurrency(2);
        // 设置为批量消费，每个批次数量在Kafka配置参数中设置
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(1500);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // 消息过滤器
    @Bean
    public KafkaListenerContainerFactory<?> filterContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<?, ?> factory = new ConcurrentKafkaListenerContainerFactory<>();
        //配合RecordFilterStrategy使用，被过滤的信息将被丢弃
        factory.setAckDiscarded(true);
        // 消息过滤
        factory.setRecordFilterStrategy(defaultRecordFilterStrategy());
        //设置并发量，小于或等于Topic的分区数
        factory.setConcurrency(2);
        // 设置为批量监听
        factory.setBatchListener(true);
        factory.getContainerProperties().setPollTimeout(1500);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // 监听时的消息错误处理
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler() {
        return (message, e, consumer) -> {
            log.info("消息处理错误: {}", message.getPayload());
            return null;
        };
    }

    // 消息拦截
    @Bean
    public DefaultRecordFilterStrategy defaultRecordFilterStrategy() {
        return new DefaultRecordFilterStrategy();
    }

    @Bean
    public KafkaConsumer kafkaConsumer() {
        return new KafkaConsumer();
    }
}
