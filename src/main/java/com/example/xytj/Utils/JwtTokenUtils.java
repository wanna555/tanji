package com.example.xytj.Utils;

import com.example.xytj.pojo.User;
import io.jsonwebtoken.*;

import java.util.Date;
import java.util.UUID;

/**
 * @title JwtTokenUtils
 * @Author: ZKY
 * @CreateTime: 2023-05-02  22:07
 * @Description: TODO
 */
public class JwtTokenUtils {
    private static final long time = 1000*60*30;

    private static final String signature = "userLogin";
    /*
    * 生成JwkToken
    * */
    public static String generateJwtToken(User user){

        String nickName = user.getNickName();
        String openid = user.getOpenid();
        JwtBuilder jwtBuilder = Jwts.builder();
        String jwtToken = jwtBuilder
                //设置头信息
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                //设置负载
                .claim("nickName",nickName)
                .claim("openid",openid)
                .setSubject("user: "+openid+" login")
                .setExpiration(new Date(System.currentTimeMillis()+time))
                .setId(UUID.randomUUID().toString())
                //设置签名
                .signWith(SignatureAlgorithm.HS256,signature)
                .compact();
        return jwtToken;
    }

    /*
    * Token解密
    * */
    public static Claims JwtTokenParse(String token){
        JwtParser jwtParser = Jwts.parser();
        Jws<Claims> claimsJws = jwtParser.setSigningKey(signature).parseClaimsJws(token);
        Claims body = claimsJws.getBody();
        return body;
    }
}
