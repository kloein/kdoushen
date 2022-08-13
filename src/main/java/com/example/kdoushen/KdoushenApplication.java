package com.example.kdoushen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.example.kdoushen.douyin.dao")
@EnableTransactionManagement
@EnableJms
public class KdoushenApplication {

    public static void main(String[] args) {
        SpringApplication.run(KdoushenApplication.class, args);
    }

}
