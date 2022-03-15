package com.magictool.web.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * 用于生成和验证 token 的工具类
 * @author lijf
 */
public class JwtUtils {

    /**
     * token 过期时间
     */
    public static final long TOKEN_EXPIRED_TIME = 1000 * 60 * 60 * 24L;

    /**
     * 签发ID
     */
    public static final String ISSUE_ID = "admin";

    /**
     * userId 标识
     */
    public static final String USER_ID = "userId";

    /**
     * 加密/解密 密钥
     */
    private static final String JWT_SECRET = "HS256/MD5";

    /**
     * 创建token
     * @param claims    载荷
     * @param time  token时长
     * @return  String
     */
    public static String createJwt(Map<String, Object> claims, Long time) {
        // 指定签名的时候使用的签名算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Date now = new Date(System.currentTimeMillis());
        long nowMillis = System.currentTimeMillis();
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setId(ISSUE_ID)
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, JWT_SECRET);

        if (time >= 0) {
            long expMillis = nowMillis + time;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 通过 token 获取 userId
     *
     * @param token token
     * @return userId
     */
    public static String getUserId(String token){
        Claims claims = verifyJwt(token);
        return claims.get(USER_ID) + "";
    }


    /**
     * 验证Token，如果返回null说明token无效或过期
     *
     * @param token token令牌
     * @return  Claims
     */
    public static Claims verifyJwt(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET)
                    .parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    /**
     * 生成token
     *
     * @param data 载荷
     * @return  String
     */
    public static String generateToken(Map<String, Object> data) {
        return createJwt(data, TOKEN_EXPIRED_TIME);
    }

}
