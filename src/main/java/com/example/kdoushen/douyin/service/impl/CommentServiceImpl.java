package com.example.kdoushen.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.Comment;
import com.example.kdoushen.douyin.dao.CommentMapper;
import com.example.kdoushen.douyin.service.CommentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    @Resource
    RedisTemplate redisTemplate;

    private static int timeout=60;

    @Override
    public long queryCommentCountByVid(long vid) {
        ValueOperations<String,Integer> valueOperations = redisTemplate.opsForValue();
        String redisKey="commentCount:"+vid;
        Integer commentCountFromRedis = valueOperations.get(redisKey);
        if (commentCountFromRedis == null) {
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq("vid", vid);
            Long commentCountFromDB = count(commentQueryWrapper);
            valueOperations.set(redisKey, commentCountFromDB.intValue(),timeout, TimeUnit.SECONDS);
            return commentCountFromDB;
        }
        return commentCountFromRedis.longValue();
    }

    @Override
    public List<Comment> queryCommentsByVid(long vid) {
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<Comment>().
                eq("vid", vid).orderByDesc("comment_time");
        return list(commentQueryWrapper);
    }

    @Override
    public void addCommentCountInRedis(long vid) {
        String redisKey="commentCount:"+vid;
        ValueOperations<String,Integer> valueOperations = redisTemplate.opsForValue();
        Integer cnt = valueOperations.get(redisKey);
        if (cnt != null) {
            valueOperations.set(redisKey, cnt+1);
        }
    }

    @Override
    public void reduceCommentCountInRedis(long vid) {
        String redisKey="commentCount:"+vid;
        ValueOperations<String,Integer> valueOperations = redisTemplate.opsForValue();
        Integer cnt = valueOperations.get(redisKey);
        if (cnt != null) {
            valueOperations.set(redisKey, cnt+1);
        }
    }
}
