package com.example.kdoushen.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.UserMsg;
import com.example.kdoushen.douyin.dao.UserMsgMapper;
import com.example.kdoushen.douyin.service.UserMsgService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Service
public class UserMsgServiceImpl extends ServiceImpl<UserMsgMapper, UserMsg> implements UserMsgService {
    @Resource
    RedisTemplate redisTemplate;

    private static int timeout=60;
    @Override
    public UserMsg getUserMsgByUid(Long uid) {
        String redisKey="userMsg:"+uid;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        UserMsg redisUserMsg = (UserMsg) valueOperations.get(redisKey);
        if (redisUserMsg == null) {
            QueryWrapper<UserMsg> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("uid", uid);
            UserMsg userMsg = getOne(queryWrapper);
            valueOperations.set(redisKey, userMsg, timeout, TimeUnit.SECONDS);
            return userMsg;
        }
        return redisUserMsg;
    }
}
