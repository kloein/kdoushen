package com.example.kdoushen.util;

import com.example.kdoushen.douyin.bean.protobuf.user.Login;
import com.example.kdoushen.douyin.util.JsonUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JsonTest {
    @Test
    public void testBuilder2Json() {
        Login.douyin_user_login_response.Builder responseBuilder = Login.douyin_user_login_response.newBuilder();
        responseBuilder.setStatusCode(1);
        responseBuilder.setStatusMsg("NO FOUND");
        responseBuilder.setUserId(1);
        responseBuilder.setToken("abc");
        String responseJson=JsonUtil.builder2Json(responseBuilder);
        System.out.println(responseJson);
    }
}
