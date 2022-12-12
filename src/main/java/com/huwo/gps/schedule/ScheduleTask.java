package com.huwo.gps.schedule;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.huwo.gps.domain.CallMeGps;
import com.huwo.gps.domain.HwPositionMessage;
import com.huwo.gps.utils.RedisClient;
import com.huwo.gps.utils.RedisKeyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-08-11  17:23
 */

@Component
public class ScheduleTask {

    @Autowired
    RedisClient redisClient;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Scheduled(cron = "0 0/1 * * * ? ")
    public void configureTasks() {

        Set set = redisClient.getValueForSet(RedisKeyUtils.getGpsKey("setKey"));
        if (CollectionUtils.isEmpty(set)) {
            return;
        }
        for (Object obj : set) {
            String key = (String) obj;
            String[] split = key.split("-");

            CallMeGps callMeGps = CallMeGps.builder()
                    .id(IdUtil.getSnowflakeNextId())
                    .timeStamp(split[1])
                    .vehicleID(split[0])
                    .build();
            List<HwPositionMessage> hwPositionMessageList = new ArrayList<>();
            Set valueForSet = redisClient.getValueForSet(RedisKeyUtils.getGpsKey(key));
            for (Object o : valueForSet) {
                HwPositionMessage hwPositionMessage = JSON.parseObject((String) o, HwPositionMessage.class);
                hwPositionMessageList.add(hwPositionMessage);

            }
            callMeGps.setData(hwPositionMessageList);
            mongoTemplate.save(callMeGps);
            //删除key
            redisClient.deleteForSet(RedisKeyUtils.getGpsKey(key));
            //删除key对应的value
            redisClient.removeValueForSet(RedisKeyUtils.getGpsKey("setKey"), key);

            System.out.println("已存入数据库：" + JSON.toJSONString(callMeGps));

        }


    }

}
