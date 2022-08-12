package com.example.kdoushen.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.Video;
import com.example.kdoushen.douyin.dao.VideoMapper;
import com.example.kdoushen.douyin.service.VideoService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;


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

    @Override
    public List<Video> getUserVideosByUid(long uid) {
        QueryWrapper<Video> queryByUid = new QueryWrapper<Video>().eq("uid", uid);
        return list(queryByUid);
    }
}
