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

    /**
     *
     * @Description: 查询指定key是否存在
     * @Author: liuchang
     * @Date: 2022-12-13 10:06
     * @Param: [dir, key]
     * @Return: boolean
     **/
    public boolean checkKey(String dir,String key) {
        Set keys = redisTemplate.keys(dir+":*");
        if (keys.contains(key)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     *
     * @Description: Set数据类型增加数据
     * @Author: liuchang
     * @Date: 2022-12-13 10:07
     * @Param: [key, value]
     * @Return: void
     **/
    public void addForSet(String key, String value) {
        SetOperations setOps = redisTemplate.opsForSet();
        setOps.add(key, value);
    }

    /**
     *
     * @Description: 根据key删除
     * @Author: liuchang
     * @Date: 2022-12-13 10:07
     * @Param: [key]
     * @Return: void
     **/
    public void deleteForSet(String key) {
        redisTemplate.delete(key);
    }


    /**
     *
     * @Description: 根据key得到所有值
     * @Author: liuchang
     * @Date: 2022-12-13 10:07
     * @Param: [key]
     * @Return: java.util.Set
     **/
    public Set getValueListForSet(String key) {
        SetOperations setOps = redisTemplate.opsForSet();
        Set set = setOps.members(key);
        return set;
    }

    /**
     *
     * @Description: 根据key和value,删除该条数据
     * @Author: liuchang
     * @Date: 2022-12-13 10:08
     * @Param: [key, value]
     * @Return: void
     **/
    public void removeValueForSet(String key, String value) {
        redisTemplate.boundSetOps(key).remove(value);
    }


}
