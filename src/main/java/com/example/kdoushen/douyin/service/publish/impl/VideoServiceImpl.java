package com.example.kdoushen.douyin.service.publish.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.publish.Video;
import com.example.kdoushen.douyin.bean.user.User;
import com.example.kdoushen.douyin.dao.publish.VideoMapper;
import com.example.kdoushen.douyin.service.publish.VideoService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;


@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    //用于调整时差，此参数是格林威治与北京的时差的值
    private static final long EIGHT_HOUR_DIR_MIL=28800000L;

    /**
     * 将视频信息储存到数据库中
     * @param uid
     * @param playUrl
     * @param coverUrl
     * @param title
     */
    public void saveVideoMsg(Long uid,String playUrl,String coverUrl,String title){
        Video video = new Video();
        video.setUId(uid);
        video.setPlayUrl(playUrl);
        video.setCoverUrl(coverUrl);
        video.setPublishTime(new Timestamp(System.currentTimeMillis()+EIGHT_HOUR_DIR_MIL));
        video.setTitle(title);
        save(video);
    }
}
