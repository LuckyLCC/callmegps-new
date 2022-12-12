package com.huwo.gps;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.huwo.gps.domain.CallMeGps;
import com.huwo.gps.domain.HwPositionMessage;
import com.huwo.gps.utils.RedisClient;
import com.huwo.gps.utils.RedisKeyUtils;
import com.huwo.gps.utils.TimeConvert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SpringBootTest
class CallmegpsNewApplicationTests {


    @Autowired
    RedisClient redisClient;

    @Autowired
    private MongoTemplate mongoTemplate;
//
//    @Scheduled(cron = "0 0/1 * * * ? ")
//    private void configureTasks() {
//        Set hashKey = redisClient.getHashSet("hashKey");
//        if (!hashKey.isEmpty()) {
//
//        }
//    }

    @Test
    void contextLoads() {

//        Set hashKey = redisClient.getSmallKeyByHashKey("hashKey");
//
//        System.out.println(hashKey);
//
//        for (Object o : hashKey) {
//            String value = redisClient.getHashValue("hashKey", (String) o);
//            System.out.println(value);
//        }

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

        }


    }

    @Test
    public void get(){
        String date = TimeConvert.getLocalDateTimeOfTimestamp(1669985364000L);
        System.out.println(date);
    }
}
