package com.example.kdoushen.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kdoushen.douyin.bean.Video;


public interface VideoService extends IService<Video> {
    /**
     * 向数据库存入视频的相关信息，vid与timestamp会自动生成不需要传参
     * @param uid
     * @param playUrl
     * @param coverUrl
     * @param title
     */
    public void saveVideoMsg(Long uid,String playUrl,String coverUrl,String title);

}
