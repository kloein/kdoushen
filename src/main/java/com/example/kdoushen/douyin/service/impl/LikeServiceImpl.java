package com.example.kdoushen.douyin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.Like;
import com.example.kdoushen.douyin.dao.LikeMapper;
import com.example.kdoushen.douyin.service.LikeService;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Like> implements LikeService {
}
