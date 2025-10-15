package com.imp.all.producer;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Longlin
 * @date 2021/6/3 10:55
 * @description
 */
@Configuration
public class KafkaProducerConfiguration {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootStrapServer;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        //配置Kafka实例的连接地址
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServer);
        return new KafkaAdmin(props);
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdmin().getConfig());
    }

    // 消息生产者
    @Bean
    public KafkaProducer kafkaProducer() {
        return new KafkaProducer();
    }

    // 发送消息监听者
    @Bean
    public KafkaSendResultHandler kafkaSendResultHandler() {
        return new KafkaSendResultHandler();
    }
}
