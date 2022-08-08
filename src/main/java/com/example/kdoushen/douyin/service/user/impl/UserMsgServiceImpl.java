package com.example.kdoushen.douyin.service.user.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.kdoushen.douyin.bean.user.UserMsg;
import com.example.kdoushen.douyin.dao.user.UserMsgMapper;
import com.example.kdoushen.douyin.service.user.UserMsgService;
import org.springframework.stereotype.Service;

@Service
public class UserMsgServiceImpl extends ServiceImpl<UserMsgMapper, UserMsg> implements UserMsgService {
}
