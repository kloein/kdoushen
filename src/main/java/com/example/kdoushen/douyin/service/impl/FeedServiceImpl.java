package com.example.kdoushen.douyin.service.impl;

import com.example.kdoushen.douyin.bean.Video;
import com.example.kdoushen.douyin.service.FeedService;
import com.example.kdoushen.douyin.service.Strategy;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {

    public List<Video> getVideoByStrategy(Strategy strategy, Timestamp timestamp) {
        return strategy.getVideo(timestamp);
    }
}
