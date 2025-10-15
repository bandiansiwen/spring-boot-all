package com.imp.all.controller;

import com.imp.all.framework.common.pojo.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author Longlin
 */
@RestController
@Slf4j
@RequestMapping("/kafka/topic")
public class KafkaTopicController {

    @Resource
    private AdminClient adminClient;

    @PostMapping("/create")
    public CommonResult<?> createTopic(@RequestParam String topicName,
                                       @RequestParam int partitions,
                                       @RequestParam int replicationFactor) {
        try {
            CreateTopicsResult topics = adminClient.createTopics(
                    Collections.singletonList(new NewTopic(topicName, partitions, (short) replicationFactor))
            );
            topics.all().get();
            return CommonResult.success();
        } catch (InterruptedException | ExecutionException e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @GetMapping("/findAll")
    public CommonResult<?> findAllTopic() {
        try {
            ListTopicsResult result = adminClient.listTopics();
            Collection<TopicListing> list = result.listings().get();
            List<String> resultList = new ArrayList<>();
            for (TopicListing topicListing : list) {
                resultList.add(topicListing.name());
            }
            return CommonResult.success(resultList);
        } catch (InterruptedException | ExecutionException e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @GetMapping("/info/{topicName}")
    public CommonResult<?> topicInfo(@PathVariable String topicName) {
        try {
            DescribeTopicsResult result = adminClient.describeTopics(Collections.singletonList(topicName));
            Map<String, TopicDescription> map = result.all().get();
            Map<String,String> resultMap = new HashMap<>();
            result.all().get().forEach((k,v)->{
                log.info("k: "+k+" ,v: "+v.toString());
                resultMap.put(k,v.toString());
            });
            return CommonResult.success(resultMap);
        } catch (InterruptedException | ExecutionException e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{topicName}")
    public CommonResult<?> deleteTopic(@PathVariable String topicName) {
        try {
            DeleteTopicsResult result = adminClient.deleteTopics(Collections.singletonList(topicName));
            result.all().get();
            return CommonResult.success();
        } catch (InterruptedException | ExecutionException e) {
            return CommonResult.failed(e.getMessage());
        }
    }
}