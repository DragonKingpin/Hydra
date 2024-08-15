package com.pinecone.hydra.service;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ApplicationDescription extends Pinenut {
    String getId();
    void setId(String id);

    String getUUID();
    void setUUID(String UUID);

    String getName();
    void setName(String name);

    String getPath();
    void setPath(String path);

    String getType();
    void setType(String type);

    String getAlias();
    void setAlias(String alias);

    String getResourceType();
    void setResourceType(String resourceType);

    String getDeploymentMethod();
    void setDeploymentMethod(String deploymentMethod);

    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);
}