package com.huwo.gps.controller;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-12-12  16:13
 */
@RestController
public class KafkaProController {


    @Autowired
    private KafkaTemplate kafkaTemplate;


    @RequestMapping("/hello")
    public String hello(){
        System.out.println("------->测试生产者发送消息");
        kafkaTemplate.send("liuchang", "wolaile");
        return "kafka消息已发送.";
    }
}
