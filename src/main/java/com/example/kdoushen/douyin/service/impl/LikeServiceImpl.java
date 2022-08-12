package com.example.kdoushen.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.Like;

import com.example.kdoushen.douyin.dao.LikeMapper;
import com.example.kdoushen.douyin.service.LikeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {
    @Resource
    RedisTemplate redisTemplate;

    private static int timeOut=30;


    @Override
    public long queryFavoriteCountByVid(long vid) {
        ValueOperations<String,Integer> valueOperations = redisTemplate.opsForValue();
        String redisKey="likeCount:"+vid;
        Integer likeCountFromRedis=valueOperations.get(redisKey);
        if (likeCountFromRedis == null) {
            QueryWrapper<Like> favoriteQuery = new QueryWrapper<>();
            favoriteQuery.eq("vid", vid);
            Long likeCountFromDB=count(favoriteQuery);
            valueOperations.set(redisKey, likeCountFromDB.intValue(), timeOut, TimeUnit.SECONDS);
            return likeCountFromDB;
        }
        return likeCountFromRedis;
    }

    @Override
    public boolean queryIsFavoriteByVidAndUid(long vid, long uid) {
        QueryWrapper<Like> isFavoriteQuery=new QueryWrapper<>();
        isFavoriteQuery.eq("uid", uid).eq("vid", vid);
        return count(isFavoriteQuery) == 1 ? true : false;
    }

    @Override
    public void removeFavoriteByVidAndUid(long vid, long uid) {
        QueryWrapper<Like> query=new QueryWrapper<>();
        query.eq("uid", uid).eq("vid", vid);
        remove(query);
    }

    @Override
    public List<Like> queryUserLikesByUid(long uid) {
        QueryWrapper<Like> likeQueryWrapper = new QueryWrapper<>();
        likeQueryWrapper.eq("uid", uid);
        return list(likeQueryWrapper);
    }

    @Override
    public void addLikeCountInRedis(long vid) {
        String redisKey="likeCount:"+vid;
        ValueOperations<String,Integer> valueOperations = redisTemplate.opsForValue();
        Integer cnt = valueOperations.get(redisKey);
        if (cnt != null) {
            valueOperations.set(redisKey, cnt+1);
        }
    }

    @Override
    public void reduceLikeCountInRedis(long vid) {
        String redisKey="likeCount:"+vid;
        ValueOperations<String,Integer> valueOperations = redisTemplate.opsForValue();
        Integer cnt = valueOperations.get(redisKey);
        if (cnt != null) {
            valueOperations.set(redisKey, cnt-1);
        }
    }
}
