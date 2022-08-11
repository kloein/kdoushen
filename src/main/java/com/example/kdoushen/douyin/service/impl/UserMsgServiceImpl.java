package com.example.kdoushen.douyin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.UserMsg;
import com.example.kdoushen.douyin.dao.UserMsgMapper;
import com.example.kdoushen.douyin.service.UserMsgService;
import org.springframework.stereotype.Service;

@Service
public class UserMsgServiceImpl extends ServiceImpl<UserMsgMapper, UserMsg> implements UserMsgService {
}