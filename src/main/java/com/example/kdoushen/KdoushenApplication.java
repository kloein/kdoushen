package com.example.kdoushen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.example.kdoushen.douyin.dao")
@EnableTransactionManagement
public class KdoushenApplication {

    public static void main(String[] args) {
        SpringApplication.run(KdoushenApplication.class, args);
    }

}
