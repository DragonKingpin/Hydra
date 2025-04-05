package com.walnut.sparta.uofs.console.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.walnut.sparta.uofs.console.util.JWTUtil;
import com.walnut.sparta.uofs.console.api.response.BasicResultResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    private Logger log = LoggerFactory.getLogger( this.getClass() );
    @Override
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
        String url=request.getRequestURI();
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
        //log.info("请求的路径是："+ url);
        if (url.contains("login")||url.contains("register")||url.contains("send_code")||url.contains("download")){
            log.info("Allow login or registration operations");
            return true;
        }
        String jwt=request.getHeader("Token");
        if (!StringUtils.hasLength(jwt)){
            log.info("The request header Token is empty");
            BasicResultResponse error = BasicResultResponse.error("not login");
            String jsonString = JSONObject.toJSONString(error);
            response.getWriter().write(jsonString);
            return false;
        }
        try {
            JWTUtil.ParseJWt(jwt);
        } catch (Exception e){
            log.info("Token parsing failed");
            BasicResultResponse error = BasicResultResponse.error("Not logged in");
            String jsonString = JSONObject.toJSONString(error);
            response.getWriter().write(jsonString);
            return false;
        }
        return true;
    }
}
