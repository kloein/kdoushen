package com.example.kdoushen.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedisConnect {
    @Resource
    RedisTemplate redisTemplate;

    @Test
    public void testConnect() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("hi", "hi");
    }

    @Test
    public void testGet() {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        System.out.println(valueOperations.get("hi"));
    }

}
