package com.example.kdoushen.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.kdoushen.douyin.bean.Video;
import com.example.kdoushen.douyin.service.Strategy;
import com.example.kdoushen.douyin.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class GetLatestStrategy implements Strategy {
    @Autowired
    VideoService videoService;

    @Value("${video-config.feed-video-max-num}")
    private String VIDEO_MAX_NUM;

    @Override
    public List<Video> getVideo(Timestamp timestamp) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("publish_time", timestamp).orderByDesc("publish_time")
                .last("limit "+VIDEO_MAX_NUM);
        return videoService.list(queryWrapper);
    }
}
