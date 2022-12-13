package com.huwo.gps.handle;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.huwo.gps.domain.CallMeGps;
import com.huwo.gps.domain.HwPositionMessage;
import com.huwo.gps.utils.RedisClient;
import com.huwo.gps.utils.RedisKeyUtils;
import com.huwo.gps.utils.TimeConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-12-13  09:06
 */

@Configuration
public class HandleData {

    @Autowired
    RedisClient redisClient;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Value("${time.duration}")
    private Long durationTime;

    @PostConstruct
    public void run() {
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        // 说一下3个参数的含义，
        // 第一个参数，要执行的run方法，
        // 第二个参数，initialDelay，首次执行等待多久后开始执行，就是容器启动后等待多久后才执行
        // 第三个参数，period，周期，首次执行完后，以后每次执行间隔多久执行
        // 这里写的period参数为2代表每2秒执行一次
        // 如果run方法里的执行时间超过了2秒，那么run方法里执行完后，会立马执行，而不会再次等待2秒
        // 如果run方法里的执行时间没有超过2秒，例如用时500毫秒，那么将会延迟1.5秒后继续执行run方法（周期 - run方法耗时 = 1.5秒）
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("线程运行" + Thread.currentThread().getName());
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
                    if (minutes > durationTime) {
                        CallMeGps callMeGps = CallMeGps.builder().id(IdUtil.getSnowflakeNextId()).timeStamp(timeStamp).vehicleID(split[0]).build();
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
                    } else {
                        System.out.println("时间不够" + key);
                    }


                }

            }
        }, 1, 2, TimeUnit.SECONDS);

    }
}
