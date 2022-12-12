package com.huwo.gps.utils;

import java.util.Random;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-12-12  17:45
 */
public class RedisKeyUtils {

    public static final String gps = "data:gps:";

    public static String getGpsKey(String key) {
        return gps + key;
    }


}
