package com.pinecone.hydra.account.entity;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface Privilege extends Pinenut {
    int getId();

    String getPrivilegeCode();
    String getToken();
    GUID getParentPrivGuid();
    GUID getGuid();
    void setGuid(GUID guid);


    void setToken(String token);

    String getName();
    void setName(String name);


    void setPrivilegeCode(String privilegeCode);

    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);

    String getType();
    void setType(String type);


    void setParentPrivGuid(GUID parentPrivGuid);
}