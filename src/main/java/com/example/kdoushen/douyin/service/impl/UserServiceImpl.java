package com.example.kdoushen.douyin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.User;
import com.example.kdoushen.douyin.dao.UserMapper;
import com.example.kdoushen.douyin.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>()
                .eq("username", username).eq("password",md5Password);
        return getOne(queryWrapper);
    }

    @Override
    public User getUserByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("username", username);
        return getOne(queryWrapper);
    }
}
