package com.walnuts.sparta.uofs.console.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;

public class JWTUtil {
    private static final String SIGN = "!^&%&*!@$*%!!@(&%2ar^2t";
    //学生登录生成JWT令牌
    public static String createJWT(){
        HashMap<String, Object> map = new HashMap<>();
        String token = JWT.create()
                .withHeader(map) //设置头信息
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600 * 1000)) //设置失效时间
                .sign(Algorithm.HMAC256(SIGN)); //设置签名以及签名方式 这里使用HMAC256加密方式
        return token;
    }

    public static DecodedJWT ParseJWt(String jwt){
        return JWT.require(Algorithm.HMAC256(SIGN)).build().verify(jwt);
    }
}
