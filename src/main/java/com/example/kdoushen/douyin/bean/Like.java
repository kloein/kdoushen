package com.example.kdoushen.douyin.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Data
@TableName("t_like")
public class Like implements Serializable {
    private static final long serialVersionUID = 92389146732922971L;
    @TableId("id")
    private Long id;

    @TableField("uid")
    private Long uId;

    @TableField("vid")
    private Long vId;

    @TableLogic
    private Integer cancel;
}
