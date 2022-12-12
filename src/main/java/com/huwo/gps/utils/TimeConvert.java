package com.huwo.gps.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-12-12  17:12
 */
public class TimeConvert {

    // timestamp可以通过long l = System.currentTimeMillis();这种方式去获取到，当然也可以通过其他的方式获取
    //得到yyyyMMddHHmm
    public static String getLocalDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String format = localDateTime.format(dateTimeFormatter);
        return format;

    }

}
