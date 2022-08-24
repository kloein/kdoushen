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
        String redisKey="commentList:"+vid;
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<Comment> redisList = (List<Comment>) valueOperations.get(redisKey);
        //redis查不到从DB
        if (redisList==null) {
            QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<Comment>().
                    eq("vid", vid).orderByDesc("comment_time");
            List<Comment> commentList = list(commentQueryWrapper);
            //将查询结果放入redis缓存
            valueOperations.set(redisKey, commentList, timeout, TimeUnit.SECONDS);
            return commentList;
        }
        return redisList;
    }

    @Override
    public void addCommentCountInRedis(long vid) {
        String redisKey="commentCount:"+vid;
        ValueOperations<String,Integer> valueOperations = redisTemplate.opsForValue();
        Integer cnt = valueOperations.get(redisKey);
        if (cnt != null) {
            valueOperations.set(redisKey, cnt+1,timeout, TimeUnit.SECONDS);
        }
    }

    @Override
    public void reduceCommentCountInRedis(long vid) {
        String redisKey="commentCount:"+vid;
        ValueOperations<String,Integer> valueOperations = redisTemplate.opsForValue();
        Integer cnt = valueOperations.get(redisKey);
        if (cnt != null) {
            valueOperations.set(redisKey, cnt+1,timeout, TimeUnit.SECONDS);
        }
    }
}
