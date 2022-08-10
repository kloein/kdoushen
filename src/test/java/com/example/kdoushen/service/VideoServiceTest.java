package com.example.kdoushen.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kdoushen.douyin.bean.Video;
import com.example.kdoushen.douyin.service.VideoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class VideoServiceTest {
    @Autowired
    VideoService videoService;
    @Test
    public void testVideoMsgGet() {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vid", 1);
        System.out.println(videoService.getOne(queryWrapper));
    }

    @Test
    public void testVideoMsgSet() {
        videoService.saveVideoMsg(2L, "123", "321", "hi~");
    }
}
