package com.walnuts.sparta.account.domian.vo;

import com.pinecone.framework.system.prototype.Pinenut;

public class AccountLoginVo implements Pinenut {
    private String userName;
    private String nickName;

    public AccountLoginVo(String userName, String nickName) {
        this.userName = userName;
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public AccountLoginVo() {
    }
}
