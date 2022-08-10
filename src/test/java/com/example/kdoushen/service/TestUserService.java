package com.example.kdoushen.service;

import com.example.kdoushen.douyin.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestUserService {
    @Autowired
    UserService userService;
    @Test
    public void testUserSelect() {
        System.out.println(userService);
        System.out.println(userService.getById(1));
    }
}
