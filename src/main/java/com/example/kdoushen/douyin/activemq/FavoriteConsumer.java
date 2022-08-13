package com.example.kdoushen.douyin.activemq;

import com.example.kdoushen.douyin.bean.Like;
import com.example.kdoushen.douyin.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.io.Serializable;

@Component
public class FavoriteConsumer {
    @Autowired
    LikeService likeService;
    @JmsListener(destination = "favoriteQueue", containerFactory = "queueListener")
    public void consumeFavoriteAction(Message message) throws JMSException {
        ObjectMessage objectMessage=(ObjectMessage) message;
        Like like = (Like)objectMessage.getObject();
        likeService.save(like);
    }

    @JmsListener(destination = "removeFavoriteQueue", containerFactory = "queueListener")
    public void consumeRemoveFavoriteAction(String message) throws JMSException {
        String[] split = message.split(":");
        Long vid=Long.parseLong(split[0]);
        Long uid=Long.parseLong(split[1]);
        likeService.removeFavoriteByVidAndUid(vid, uid);
    }
}
