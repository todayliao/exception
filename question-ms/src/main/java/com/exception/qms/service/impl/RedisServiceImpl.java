package com.exception.qms.service.impl;

import com.exception.qms.domain.entity.User;
import com.exception.qms.domain.mapper.UserMapper;
import com.exception.qms.service.RedisService;
import com.exception.qms.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void set(String key, String value, long seconds) {
        stringRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    @Override
    public boolean exists(String key) {
        return stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            byte[] bytes = getRedisMasterSerializer().serialize(key);
            return redisConnection.exists(bytes);
        });
    }

    @Override
    public boolean expire(String key, long seconds) {
        return stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    /**
     * 获取主库的字符串序列化对象
     *
     * @return
     */
    private RedisSerializer<String> getRedisMasterSerializer() {
        RedisSerializer<String> redisSerializer = stringRedisTemplate.getStringSerializer();
        return redisSerializer;
    }




}
