package com.example.kdoushen.redis;

import com.example.kdoushen.douyin.bean.UserMsg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void testStoreObj() {
        UserMsg userMsg = new UserMsg();
        userMsg.setUserId(1L);
        userMsg.setUsername("jane");
        userMsg.setFollowCount(0L);
        userMsg.setFollowerCount(0L);
        List<UserMsg> userMsgs = new ArrayList<>();
        userMsgs.add(userMsg);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("test", userMsgs);
        List<UserMsg> test = (List<UserMsg>)valueOperations.get("test");
        System.out.println(test);
    }

}
