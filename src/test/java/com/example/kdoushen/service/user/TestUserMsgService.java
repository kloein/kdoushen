package com.example.kdoushen.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.kdoushen.douyin.bean.user.UserMsg;
import com.example.kdoushen.douyin.service.user.UserMsgService;
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
        userMsgLambdaQueryWrapper.select(com.example.kdoushen.douyin.bean.user.UserMsg::getUserId, com.example.kdoushen.douyin.bean.user.UserMsg::getUsername, com.example.kdoushen.douyin.bean.user.UserMsg::getFollowCount, com.example.kdoushen.douyin.bean.user.UserMsg::getFollowerCount)
                .eq(com.example.kdoushen.douyin.bean.user.UserMsg::getUserId, user_id);
        com.example.kdoushen.douyin.bean.user.UserMsg userMsg = userMsgService.getOne(userMsgLambdaQueryWrapper);
        System.out.println(userMsg);
    }
}
