package com.example.kdoushen.douyin.service.feed;

import com.example.kdoushen.douyin.bean.publish.Video;

import java.sql.Timestamp;
import java.util.List;

public interface FeedService {
    /**
     * 从数据库中通过策略模式获取视频,timestamp指定视频截止事件,只选择在其后上传的视频
     */
    public List<Video> getVideoByStrategy(Strategy strategy, Timestamp timestamp);
}
