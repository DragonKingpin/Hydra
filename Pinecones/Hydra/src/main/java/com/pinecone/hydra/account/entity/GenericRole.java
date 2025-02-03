package com.pinecone.hydra.account.entity;

import java.time.LocalDateTime;

public class GenericRole implements Role{
    private int id;
    private String name;
    private String privilegeGuids;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String type;

    // 无参构造方法
    public GenericRole() {
        super();
    }

    // 全参构造方法
    public GenericRole(String name, String privilegeGuids, LocalDateTime createTime, LocalDateTime updateTime, String type) {

        this.name = name;
        this.privilegeGuids = privilegeGuids;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.type = type;
    }



    @Override
    public int getId() {
        return id;
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
    public String getPrivilegeGuids() {
        return privilegeGuids;
    }

    @Override
    public void setPrivilegeGuids(String privilegeGuids) {
        this.privilegeGuids = privilegeGuids;
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
}
