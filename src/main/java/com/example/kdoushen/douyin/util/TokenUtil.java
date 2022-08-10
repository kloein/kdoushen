package com.example.kdoushen.douyin.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    private static final Logger log= LoggerFactory.getLogger(TokenUtil.class);
    //设置过期时间
    private static final long EXPIRE_DATE=7*24*60*60*1000;
    //token秘钥
    private static final String TOKEN_SECRET = "ZCfasfhuaUUHufguGuwu2020BQWE";

    /**
     * 根据用户名生成token
     * @param username
     * @return
     */
    public static String token (String username,Long userId){
        String token = "";
        try {
            //过期时间
            Date date = new Date(System.currentTimeMillis()+EXPIRE_DATE);
            //秘钥及加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            //设置头部信息
            Map<String,Object> header = new HashMap<>();
            header.put("typ","JWT");
            header.put("alg","HS256");
            //携带username，password信息，生成签名
            token = JWT.create()
                    .withHeader(header)
                    .withClaim("username",username)
                    .withClaim("userId", userId.toString())
                    .withExpiresAt(date)
                    .sign(algorithm);
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
        return token;
    }
    /**
     * @desc   验证token，通过返回true
     * @params [token]需要校验的串
     **/
    public static boolean verify(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);//这里如果解析出错会抛出异常
            log.info("token解析正确。");
            return true;
        }catch (Exception e){
            log.error("token解析未非法！");
            e.printStackTrace();
            return  false;
        }
    }

    /**
     * 获取token payload中指定的字段
     * @param token
     * @param chaimName
     * @return
     */
    public static String getTokenPayload(String token,String chaimName) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getClaim(chaimName).asString();
        }catch (Exception e){
            log.error("token解析未非法！");
            e.printStackTrace();
        }
        return null;
    }
}
