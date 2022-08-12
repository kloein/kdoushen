package com.example.kdoushen.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kdoushen.douyin.bean.User;

public interface UserService extends IService<User> {
    /**
     * 根据用户名和密码获取用户信息，如果不存在返回null
     */
    public User getUserByUsernameAndPassword(String username,String password);
    /**
     * 根据用户名获取用户信息，如果不存在返回null
     */
    public User getUserByUsername(String username);
}
