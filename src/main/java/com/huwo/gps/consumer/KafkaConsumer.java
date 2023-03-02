package com.huwo.gps.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huwo.gps.service.HwPositionEventService;
import com.huwo.gps.utils.TimeConvert;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static com.alibaba.fastjson.JSON.parseArray;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-10-17  15:15
 */
@Component
public class KafkaConsumer {


    @Autowired
    HwPositionEventService hwPositionEventService;


    @KafkaListener(topics = {"huwolog20170816"})
    public void onMessage2(ConsumerRecord<String, byte[]> record) {
        String topic = record.topic();
        String msg = new String(record.value());
//        System.out.println("消费者接受消息：topic-->"+topic+",msg->>"+msg);

        JSONObject jsonObject = JSON.parseObject(msg);
        JSONObject data = jsonObject.getObject("data", JSONObject.class);
        JSONArray vehicles1 = parseArray(data.getString("vehicles"));
        JSONObject jsonObject1 = (JSONObject) vehicles1.get(0);
        hwPositionEventService.handle(jsonObject1);


    }


}
