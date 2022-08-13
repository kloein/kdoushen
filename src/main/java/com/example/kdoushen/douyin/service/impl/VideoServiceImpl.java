package com.example.kdoushen.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.Video;
import com.example.kdoushen.douyin.dao.VideoMapper;
import com.example.kdoushen.douyin.service.VideoService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {
    @Resource
    RedisTemplate redisTemplate;

    private static int timeout=60;

    //用于调整时差，此参数是格林威治与北京的时差的值
    private static final long EIGHT_HOUR_DIR_MIL=28800000L;

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
        String redisKey="userVideos:"+uid;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<Video> redisList = (List<Video>) valueOperations.get(redisKey);
        //redis中查不到再从DB
        if (redisList == null) {
            QueryWrapper<Video> queryByUid = new QueryWrapper<Video>().eq("uid", uid);
            List<Video> videoList = list(queryByUid);
            valueOperations.set(redisKey, videoList, timeout, TimeUnit.SECONDS);
            return videoList;
        }
        return redisList;
    }
}
