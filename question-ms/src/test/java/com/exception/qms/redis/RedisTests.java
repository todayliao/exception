package com.exception.qms.redis;

import com.exception.qms.spring.People;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/23
 * @time 下午5:54
 * @discription
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RedisTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis() {
        stringRedisTemplate.opsForValue().set("1", "jiangbing");
    }

}
