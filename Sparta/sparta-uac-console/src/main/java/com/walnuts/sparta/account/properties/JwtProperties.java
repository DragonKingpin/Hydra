package com.walnuts.sparta.account.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private long userTtl;
    private String userTokenName;
    private String userSecretKey;
    public JwtProperties() {
    }

    /**
     * 生成jwt令牌相关配置
     */


    public JwtProperties(String userSecretKey, long userTtl, String userTokenName) {
        this.userSecretKey = userSecretKey;
        this.userTtl = userTtl;
        this.userTokenName = userTokenName;
    }

    public String getUserSecretKey() {
        return userSecretKey;
    }

    public void setUserSecretKey(String userSecretKey) {
        this.userSecretKey = userSecretKey;
    }

    public long getUserTtl() {
        return userTtl;
    }

    public void setUserTtl(long userTtl) {
        this.userTtl = userTtl;
    }

    public String getUserTokenName() {
        return userTokenName;
    }

    public void setUserTokenName(String userTokenName) {
        this.userTokenName = userTokenName;
    }



}