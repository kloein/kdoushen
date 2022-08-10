package com.example.kdoushen.douyin.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Repository;

@Data
@Repository
@TableName("t_like")
public class Like {
    @TableId("id")
    private Long id;

    @TableField("uid")
    private Long uId;

    @TableField("vid")
    private Long vId;

    @TableLogic
    private Integer cancel;
}
