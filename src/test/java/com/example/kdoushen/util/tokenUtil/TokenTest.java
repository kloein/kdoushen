package com.example.kdoushen.util.tokenUtil;

import com.example.kdoushen.douyin.util.TokenUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.kdoushen.douyin.util.TokenUtil.token;
import static com.example.kdoushen.douyin.util.TokenUtil.verify;
@SpringBootTest
public class TokenTest {
    @Test
    public void testToken() {
        String token = token("tom",1L);
        System.out.println(verify(token));
    }

    @Test
    public void decodeToken() {
        String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2NjA2NjExNTMsInVzZXJJZCI6IjE1NTY1ODY1MTkxNDUwODY5NzgiLCJ1c2VybmFtZSI6InRvbnkifQ.JhJ7WntHLlQ-Endh1MK2R2PrZ68aR2W1AIzmlAZoYQg";
        System.out.println(TokenUtil.getTokenPayload(token,"username"));
        System.out.println(TokenUtil.getTokenPayload(token,"userId"));
    }
}
