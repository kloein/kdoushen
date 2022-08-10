package com.example.kdoushen.douyin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.User;
import com.example.kdoushen.douyin.dao.UserMapper;
import com.example.kdoushen.douyin.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
