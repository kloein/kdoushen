package com.example.kdoushen.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kdoushen.douyin.bean.Like;

import java.util.List;

public interface LikeService extends IService<Like> {
    /**
     * 根据视频id获取视频点赞量
     */
    public long queryFavoriteCountByVid(long vid);

    /**
     * 根据视频id和用户id返回是否点赞，true-用户已点赞，false-未点赞
     */
    public boolean queryIsFavoriteByVidAndUid(long vid,long uid);
    /**
     * 根据视频id和用户id取消点赞
     */
    public void removeFavoriteByVidAndUid(long vid,long uid);
    /**
     * 根据用户id获取其点赞过的视频id
     */
    public List<Like> queryUserLikesByUid(long uid);

    /**
     * 评论是将redis中点赞数+1（如果redis中存在的话）
     */
    public void addLikeCountInRedis(long vid);
    /**
     * 评论是将redis中点赞数-1（如果redis中存在的话）
     */
    public void reduceLikeCountInRedis(long vid);
}
