package com.example.kdoushen.douyin.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_user")
public class User {
    @TableId(value = "uid")
    private Long userId;
    @TableField("username")
    private String userName;
    @TableField("password")
    private String password;
}
