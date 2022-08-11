package com.example.kdoushen.service.feed;

import com.example.kdoushen.douyin.bean.publish.Video;
import com.example.kdoushen.douyin.service.feed.FeedService;
import com.example.kdoushen.douyin.service.feed.impl.GetLatestStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestGetVideo {

    @Autowired
    FeedService feedService;
    @Autowired
    GetLatestStrategy getLatestStrategy;
    @Test
    public void testGetVideo() {
        Timestamp timestamp = new Timestamp(1L);
        List<Video> videos = feedService.getVideoByStrategy(getLatestStrategy, timestamp);
        System.out.println(videos.size());
    }
}
