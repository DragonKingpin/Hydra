package com.walnuts.sparta.account.interceptor;

import com.walnuts.sparta.account.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(AuthenticationInterceptor.class);

    private final String userSecretKey = "1212121hsodhsdhasdhsaldhsalhdlsahdlsad"; // 应与生成Token的密钥一致

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否需要认证
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 判断方法或类是否有@RequiresAuthentication注解
        RequiresAuthentication classAnnotation = handlerMethod.getBeanType().getAnnotation(RequiresAuthentication.class);
        RequiresAuthentication methodAnnotation = method.getAnnotation(RequiresAuthentication.class);
        if (classAnnotation == null && methodAnnotation == null) {
            return true; // 无需认证
        }

        // 获取Token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未提供认证Token");
            log.warn("Unauthorized access attempt: Missing or invalid Authorization header");
            return false;
        }
        String token = authHeader.substring(7);

        // 打印 Token
        log.info("Received Token: {}", token);

        // 验证Token
        try {
            boolean isValid = JwtUtil.verifyToken(token, userSecretKey);
            if (!isValid) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无效的Token");
                log.warn("Unauthorized access attempt: Invalid token");
                return false;
            }
            System.out.println("Token验证"+isValid);

            Claims claims = JwtUtil.parseJWT(userSecretKey,token );
            request.setAttribute("userId", claims.get("userId"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token验证失败：" + e.getMessage());
            log.error("Unauthorized access attempt: Token verification failed", e);
            return false;
        }

        return true;
    }
}
/*
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final String userSecretKey = "1212121hsodhsdhasdhsaldhsalhdlsahdlsad"; // 应与生成Token的密钥一致

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否需要认证
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 判断方法或类是否有@RequiresAuthentication注解
        RequiresAuthentication classAnnotation = handlerMethod.getBeanType().getAnnotation(RequiresAuthentication.class);
        RequiresAuthentication methodAnnotation = method.getAnnotation(RequiresAuthentication.class);
        if (classAnnotation == null && methodAnnotation == null) {
            return true; // 无需认证
        }

        // 获取Token
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未提供认证Token");
            return false;
        }
        String token = authHeader.substring(7);

        // 验证Token
        try {
            boolean isValid = JwtUtil.verifyToken(token, userSecretKey);
            if (!isValid) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "无效的Token");
                return false;
            }
            // 可以解析Claims并设置到请求属性中，供后续使用
            Claims claims = JwtUtil.parseJWT(token, userSecretKey);
            request.setAttribute("userId", claims.get("userId"));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token验证失败：" + e.getMessage());
            return false;
        }

        return true;
    }
}*/
