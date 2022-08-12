package com.example.kdoushen.douyin.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;

@Data
@TableName("t_comment")
public class Comment {
    @TableId()
    private Long id;

    private Long uid;

    private Long vid;

    @TableField("comment_text")
    private String commentText;

    @TableField("comment_time")
    private Timestamp commentTime;

    @TableLogic
    private int cancel;
}
