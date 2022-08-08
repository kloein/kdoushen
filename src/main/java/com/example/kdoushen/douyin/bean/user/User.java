package com.example.kdoushen.douyin.bean.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")
public class User {
    @TableId("uid")
    private Long userId;
    @TableField("username")
    private String userName;
    @TableField("password")
    private String password;
}
