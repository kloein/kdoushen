package com.example.kdoushen.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.kdoushen.douyin.bean.UserMsg;
import com.example.kdoushen.douyin.service.UserMsgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestUserMsgService {
    @Autowired
    UserMsgService userMsgService;

    @Test
    public void testGetUserMsg() {
        Long user_id=1556583199496077313L;
        LambdaQueryWrapper<UserMsg> userMsgLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userMsgLambdaQueryWrapper.select(UserMsg::getUserId, UserMsg::getUsername, UserMsg::getFollowCount, UserMsg::getFollowerCount)
                .eq(UserMsg::getUserId, user_id);
        UserMsg userMsg = userMsgService.getOne(userMsgLambdaQueryWrapper);
        System.out.println(userMsg);
    }
}
