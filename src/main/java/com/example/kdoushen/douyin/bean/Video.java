package com.example.kdoushen.douyin.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@TableName("t_video")
public class Video {
    @TableId("vid")
    private Long vId;
    @TableField("uid")
    private Long uId;

    private String playUrl;

    private String coverUrl;

    private Timestamp publishTime;

    private String title;
}
