package com.example.kdoushen.douyin.service.feed;


import com.example.kdoushen.douyin.bean.publish.Video;
import com.example.kdoushen.douyin.service.publish.VideoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.List;

public interface Strategy {
    public List<Video> getVideo(Timestamp timestamp);
}
