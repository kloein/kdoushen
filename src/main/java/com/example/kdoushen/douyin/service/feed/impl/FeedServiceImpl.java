package com.example.kdoushen.douyin.service.feed.impl;

import com.example.kdoushen.douyin.bean.publish.Video;
import com.example.kdoushen.douyin.service.feed.FeedService;
import com.example.kdoushen.douyin.service.feed.Strategy;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {

    public List<Video> getVideoByStrategy(Strategy strategy, Timestamp timestamp) {
        return strategy.getVideo(timestamp);
    }
}
