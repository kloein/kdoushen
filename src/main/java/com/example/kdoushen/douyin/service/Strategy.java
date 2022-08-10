package com.example.kdoushen.douyin.service;


import com.example.kdoushen.douyin.bean.Video;

import java.sql.Timestamp;
import java.util.List;

public interface Strategy {
    /**
     * 获取特定时间后上传的视频
     * @param timestamp
     * @return
     */
    public List<Video> getVideo(Timestamp timestamp);
}
