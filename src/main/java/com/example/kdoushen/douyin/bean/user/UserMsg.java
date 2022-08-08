package com.example.kdoushen.douyin.bean.user;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user_msg")
public class UserMsg {
    @TableId("uid")
    private Long userId;

    private String username;

    private Long followCount;

    private Long followerCount;

}
