package com.example.kdoushen.douyin.util;

import com.google.gson.GsonBuilder;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.GsonBuilderUtils;

public class JsonUtil {
    private static final Logger log= LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 生成蛇形命名的json
     * @param builder
     * @return
     */
    public static String builder2Json(Builder builder)  {
        String json=null;
        try {
             json= JsonFormat.printer().preservingProtoFieldNames().print(builder);
        } catch (InvalidProtocolBufferException e) {
            log.error("builder转JSON失败！");
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 生成驼峰命名的json
     * @param builder
     * @return
     */
    public static String builder2CamelJson(Builder builder)  {
        String json=null;
        try {
            json= JsonFormat.printer().print(builder);
        } catch (InvalidProtocolBufferException e) {
            log.error("builder转JSON失败！");
            e.printStackTrace();
        }
        return json;
    }
}
