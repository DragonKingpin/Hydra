package com.walnuts.sparta.account.domian.vo;

import com.pinecone.framework.util.json.homotype.BeanJSONEncoder;

public class UserLoginVO {
    private String userid;
    private String userName;
    private String UserToken;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }

    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }


    public String toString() {
        return this.toJSONString();
    }
}
