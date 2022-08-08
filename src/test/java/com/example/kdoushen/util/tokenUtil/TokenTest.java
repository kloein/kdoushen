package com.example.kdoushen.util.tokenUtil;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.kdoushen.douyin.util.TokenUtil.token;
import static com.example.kdoushen.douyin.util.TokenUtil.verify;
@SpringBootTest
public class TokenTest {
    @Test
    public void testToken() {
        String token = token("tom");
        System.out.println(verify(token));
    }
}
