package com.example.kdoushen.service;

import com.example.kdoushen.douyin.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestCommentService {
    @Autowired
    CommentService commentService;
    @Test
    public void testCommentService() {
        System.out.println(commentService.count());
    }
}
