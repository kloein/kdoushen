package com.example.kdoushen;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

@SpringBootTest
class KdoushenApplicationTests {

    @Test
    void timeStampTest() {
        System.out.println(new Timestamp(System.currentTimeMillis()).toLocalDateTime().getHour());
    }
}
