package com.pinecone.hydra.account.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericPrivilege implements Privilege {
    private int id;
    private GUID guid;
    private String token;
    private String name;
    private String privilegeCode;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String type;
    private GUID parentPrivGuid;

    @Override
    public int getId() {
        return id;
    }
    @Override
    public GUID getGuid() {
        return guid;
    }

    @Override
    public void setGuid(GUID guid) {
        this.guid = guid;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPrivilegeCode() {
        return privilegeCode;
    }

    @Override
    public void setPrivilegeCode(String privilegeCode) {
        this.privilegeCode = privilegeCode;
    }

    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public GUID getParentPrivGuid() {
        return parentPrivGuid;
    }

    @Override
    public void setParentPrivGuid(GUID parentPrivGuid) {
        this.parentPrivGuid = parentPrivGuid;
    }

    // 无参构造方法
    public GenericPrivilege() {
    }
    // 全参构造方法
    public GenericPrivilege( GUID guid, String token, String name, String privilegeCode, LocalDateTime createTime, LocalDateTime updateTime, String type) {
        this.guid = guid;
        this.token = token;
        this.name = name;
        this.privilegeCode = privilegeCode;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.type = type;

    }

}