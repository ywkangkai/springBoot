package com.example.travel_project.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.travel_project.dto.account.LoginOutputDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;
import java.util.Map;

public class JWTUtil {
    // JWT密钥
    private static String SECRET = "KK-TEST";

    //加密
    public static String generateToken(Map<String, String> tokenInfos){
        JWTCreator.Builder builder = JWT.create();
        tokenInfos.forEach((k, v)->{
            builder.withClaim(k, v);
        });
        //设置JWT令牌过期时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 300); // 设置过期时间为300秒
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    //验证
    public static void verify(String token) {
        JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    //获取token中的信息
    public static DecodedJWT getTokenInfos(String token){
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    //获取token中当前的登录信息
    public static LoginOutputDto getLoginData(String token){
        //获取当前用户角色列表
        DecodedJWT decodedJWT = getTokenInfos(token);
        String data = decodedJWT.getClaim("data").asString();
        LoginOutputDto loginOutputDto = null;
        try{
            loginOutputDto = new ObjectMapper().readValue(data, LoginOutputDto.class);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return loginOutputDto;

    }
}
