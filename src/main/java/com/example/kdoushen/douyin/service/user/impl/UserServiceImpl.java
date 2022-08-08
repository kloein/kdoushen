package com.example.kdoushen.douyin.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.user.User;
import com.example.kdoushen.douyin.dao.user.UserMapper;
import com.example.kdoushen.douyin.service.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
