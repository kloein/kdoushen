package com.example.kdoushen.douyin.util;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;

public class IdUtils {
    private static IdentifierGenerator IDENTIFIER_GENERATOR = new DefaultIdentifierGenerator();

    /**
     * 通过雪花算法获取唯一ID
     *
     * @return id
     */
    public static long getId(Object entity) {
        return IDENTIFIER_GENERATOR.nextId(entity).longValue();
    }
}
