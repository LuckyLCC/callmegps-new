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

import java.time.Duration;
import java.time.LocalDateTime;
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
            String timeStamp = split[1];
            LocalDateTime localDateTime = TimeConvert.strToLocalDateTime(timeStamp);
            LocalDateTime now = LocalDateTime.now();
            Duration duration = Duration.between(localDateTime, now);
            //相差的分钟数
            long minutes = duration.toMinutes();
            if (minutes > 2) {
                CallMeGps callMeGps = CallMeGps.builder()
                        .id(IdUtil.getSnowflakeNextId())
                        .timeStamp(timeStamp)
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


    @Test
    public void get() {
        String date = TimeConvert.longToStr(1669985364000L);
        System.out.println(date);

        LocalDateTime localDateTime = TimeConvert.strToLocalDateTime("202212130919");
        System.out.println(localDateTime);

        LocalDateTime localDateTime1 = localDateTime.plusMinutes(10);
        System.out.println(localDateTime1);
        Duration duration = Duration.between(localDateTime, LocalDateTime.now());
        long minutes = duration.toMinutes();//相差的分钟数
        System.out.println(minutes);

    }


    @Test
    public void get2() {
        int x = 2;  //x 为正整数，改变 x 再测试几次
        // 此时此刻
        LocalDateTime now = LocalDateTime.now();
        // 2019-11-12T15:48:27.075098100
        System.out.println(now);
        // 明天的这个时刻
        LocalDateTime nowAtTomorrow = now.plusDays(x);
        // 2019-11-13T15:48:27.075098100
        System.out.println(nowAtTomorrow);

        int i = now.compareTo(nowAtTomorrow);
        // x=1时打印-1，x=2时打印-2，以此类推输出结果等于 -x < 0
        System.out.println(i);

        int j = nowAtTomorrow.compareTo(now);
        // x=1时打印1，x=2时打印2，以此类推输出结果等于 x > 0
        System.out.println(j);
    }


}
