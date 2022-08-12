package com.example.kdoushen.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kdoushen.douyin.bean.Comment;

import java.util.List;


public interface CommentService extends IService<Comment> {
    /**
     * 通过视频id获取评论数量,先通过redis查询，查询不到在从DB查询
     */
    public long queryCommentCountByVid(long vid);

    /**
     * 通过视频id获取其评论,发布时间晚的在前
     */
    public List<Comment> queryCommentsByVid(long vid);

    /**
     * 评论是将redis中评论数+1（如果redis中存在的画）
     */
    public void addCommentCountInRedis(long vid);
    /**
     * 评论是将redis中评论数-1（如果redis中存在的画）
     */
    public void reduceCommentCountInRedis(long vid);

}
