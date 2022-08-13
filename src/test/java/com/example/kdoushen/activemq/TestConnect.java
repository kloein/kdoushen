package com.example.kdoushen.activemq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;
import javax.jms.Queue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestConnect {
    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue removeCommentQueue;

    // 发送消息，destination是发送到的队列，message是待发送的消息
    private void sendMessage(Destination destination, final String message){
        jmsMessagingTemplate.convertAndSend(destination, message);
    }

    @Test
    public void product() {
        sendMessage(removeCommentQueue, "hello");
        System.out.println("发送成功");
    }

    @JmsListener(destination = "removeCommentQueue", containerFactory = "queueListener")
    public void consume(String message) {
        System.out.println(message);
    }

}
