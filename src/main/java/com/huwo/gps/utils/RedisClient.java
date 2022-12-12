package com.huwo.gps.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @program:
 * @description:
 * @author: zhangAihua
 * @create_time: 2020/9/20 0:19
 */
@Service
@Slf4j
public class RedisClient {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean checkKey(String dir,String key) {
        Set keys = redisTemplate.keys(dir+":*");
        if (keys.contains(key)) {
            return true;
        } else {
            return false;
        }
    }


    public void addForSet(String key, String value) {
        SetOperations setOps = redisTemplate.opsForSet();
        setOps.add(key, value);
    }

    public void deleteForSet(String key) {
        redisTemplate.delete(key);
    }

    public Set getValueForSet(String key) {
        SetOperations setOps = redisTemplate.opsForSet();
        Set set = setOps.members(key);
        return set;
    }

    public void removeValueForSet(String key, String value) {
        redisTemplate.boundSetOps(key).remove(value);
    }


}
