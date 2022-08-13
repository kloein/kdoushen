package com.example.kdoushen.douyin.activemq;

import com.example.kdoushen.douyin.bean.Comment;
import com.example.kdoushen.douyin.bean.Like;
import com.example.kdoushen.douyin.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

@Component
public class CommentConsumer {
    @Autowired
    CommentService commentService;
    @JmsListener(destination = "commentQueue", containerFactory = "queueListener")
    public void consumeFavoriteAction(Message message) throws JMSException {
        ObjectMessage objectMessage=(ObjectMessage) message;
        Comment comment = (Comment)objectMessage.getObject();
        commentService.save(comment);
    }

    @JmsListener(destination = "removeCommentQueue", containerFactory = "queueListener")
    public void consumeRemoveFavoriteAction(String commentId) throws JMSException {
        Long cId=Long.parseLong(commentId);
        commentService.removeById(cId);
    }
}
