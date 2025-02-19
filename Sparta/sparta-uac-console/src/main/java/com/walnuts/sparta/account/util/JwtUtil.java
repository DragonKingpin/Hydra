package com.walnuts.sparta.account.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
 public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);
        return builder.compact();
    }
/*    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 使用 Keys.hmacShaKeyFor 生成 SecretKey
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        // 设置 JWT 的过期时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 构建 JWT
        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                // 注意：这里需要传入 SecretKey 和算法
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(exp);
        return builder.compact();
    }*/
    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    /**
     * 解析 JWT Token
     * @param secretKey 密钥
     * @param token JWT Token
     * @return 解析后的 Claims
     */
    public static Claims parseJWT(String secretKey, String token) {
        System.out.println("Token: " + token);

        if (token == null || token.isEmpty() || token.split("\\.").length != 3) {
            log.error("JWT格式错误: Token为空或格式不正确");
            throw new IllegalArgumentException("无效的JWT令牌");
        }
        try {
            // 使用与生成时相同的密钥
            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
            SecretKey key = Keys.hmacShaKeyFor(keyBytes);
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.error("JWT解析失败: {}", e.getMessage());
            throw new IllegalArgumentException("无效的JWT令牌");
        }
    }

    /**
     * 验证JWT的有效性
     *
     * @param token         加密后的token
     * @param userSecretKey 用户的私钥
     * @return 如果token有效返回true，否则返回false
     */
    public static boolean verifyToken(String token, String userSecretKey) {
        try {
            // 使用用户的私钥解析JWT
            SecretKey secretKey = Keys.hmacShaKeyFor(userSecretKey.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            // 如果解析成功，说明token有效
            return true;
        } catch (ExpiredJwtException e) {
            // token已过期
            System.out.println("Token has expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            // 不支持的JWT格式
            System.out.println("Unsupported JWT: " + e.getMessage());
        } catch (MalformedJwtException e) {
            // JWT格式错误
            System.out.println("Invalid JWT string: " + e.getMessage());
        } catch (SignatureException e) {
            // 签名验证失败
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // JWT字符串为空或null
            System.out.println("JWT string is null or empty or only whitespace: " + e.getMessage());
        } catch (Exception e) {
            // 其他异常
            System.out.println("Other error: " + e.getMessage());
        }
        // 如果捕获到异常，说明token无效
        return false;
    }
}