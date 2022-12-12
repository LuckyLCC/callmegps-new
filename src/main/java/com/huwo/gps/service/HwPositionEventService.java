package com.huwo.gps.service;

import com.alibaba.fastjson.JSONObject;
import com.huwo.gps.utils.RedisClient;
import com.huwo.gps.utils.RedisKeyUtils;
import com.huwo.gps.utils.TimeConvert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-12-12  17:35
 */

@Service
@Slf4j
public class HwPositionEventService {

    @Autowired
    RedisClient redisClient;


    public void handle(JSONObject data) {
        String vehicleID = (String) data.get("vehicleID");
        Long timestamp = (Long) data.get("timestamp");
        String date = TimeConvert.getLocalDateTimeOfTimestamp(timestamp);
        String key = vehicleID + "-" + date;
        boolean flag = redisClient.checkKey(RedisKeyUtils.gps,key);
        if (flag) {
            redisClient.addForSet(RedisKeyUtils.getGpsKey(key), data.toString());
        } else {
            //key不存在
            redisClient.addForSet(RedisKeyUtils.getGpsKey(key), data.toString());
            redisClient.addForSet(RedisKeyUtils.getGpsKey("setKey"),key);
        }

        System.out.println("hhhh");

    }
}
