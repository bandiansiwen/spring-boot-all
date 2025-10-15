package com.imp.all.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;

@Slf4j
public class DefaultRecordFilterStrategy implements RecordFilterStrategy<Object, Object> {

    @Override
    public boolean filter(ConsumerRecord consumerRecord) {
        String msg = (String) consumerRecord.value();
        if(msg.contains("abc")){
            return false;
        }
        log.info("filterContainerFactory filter : " + msg);
        //返回true将会被丢弃
        return true;
    }
//	自己定义的接口，可通过实现该接口来实现自定义过滤规则来过滤规则。
//	CustomFilter customFilter;
//    @Override
//    public final boolean filter(ConsumerRecord consumerRecord) {
//        //先判断幂等性，幂等性判断消息是否为重发
//        /**
//         * 幂等为true，返回true
//         */
//        if(判断幂等){
//            return true;
//        }
//        //再判断自定义规则
//        return customFilter.rule(consumerRecord)；
//    }
}
