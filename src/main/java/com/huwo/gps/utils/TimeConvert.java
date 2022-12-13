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

    /**
     *
     * @Description: 按照yyyyMMddHHmm格式把时间戳转换成String
     * @Author: liuchang
     * @Date: 2022-12-13 9:17
     * @Param: [timestamp]
     * @Return: java.time.LocalDate
     *
     * @return*/
    public static String longToStr(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String format = localDateTime.format(dateTimeFormatter);
        return format;

    }

    /**
     *
     * @Description: 按照yyyyMMddHHmm格式把String转换成LocalDateTime
     * @Author: liuchang
     * @Date: 2022-12-13 9:17
     * @Param: [timestamp]
     * @Return: java.time.LocalDate
     *
     * @return*/
    public static LocalDateTime strToLocalDateTime(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDateTime parse = LocalDateTime.parse(time,dateTimeFormatter);
        return parse;

    }



}
