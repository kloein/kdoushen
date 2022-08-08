package com.example.kdoushen.dao.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kdoushen.douyin.bean.user.UserMsg;
import com.example.kdoushen.douyin.dao.user.UserMsgMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestUserMsgMapper {
    @Autowired
    UserMsgMapper userMsgMapper;

    @Test
    public void testUserMsgMapperSelect() {
        LambdaQueryWrapper<UserMsg> queryWrapper = new LambdaQueryWrapper<UserMsg>();
        queryWrapper.select(UserMsg::getUserId,UserMsg::getUsername,UserMsg::getFollowCount,UserMsg::getFollowerCount)
                .eq(UserMsg::getUserId, 1);
        UserMsg userMsg = userMsgMapper.selectOne(queryWrapper);
        System.out.println(userMsg.getUserId());
        System.out.println(userMsg.getUsername());
        System.out.println(userMsg.getFollowCount());
        System.out.println(userMsg.getFollowerCount());
    }
}
