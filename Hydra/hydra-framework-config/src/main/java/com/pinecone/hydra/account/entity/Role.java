package com.pinecone.hydra.account.entity;

import com.pinecone.framework.system.prototype.Pinenut;

import java.time.LocalDateTime;

public interface Role extends Pinenut {
    int getId();
    String getName();
    void setName(String name);
    String getPrivilegeGuids();
    void setPrivilegeGuids(String privilegeGuids);

    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);

    String getType();
    void setType(String type);
}
