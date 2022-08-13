package com.example.kdoushen.douyin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.kdoushen.douyin.bean.UserMsg;

public interface UserMsgService extends IService<UserMsg> {
    /**
     * 根据某一用户名获取用户信息，先从redis查，再从DB查
     * @param uid
     * @return
     */
    public UserMsg getUserMsgByUid(Long uid);
}
