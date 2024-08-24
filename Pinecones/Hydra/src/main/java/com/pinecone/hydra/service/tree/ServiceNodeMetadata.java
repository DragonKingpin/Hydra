package com.pinecone.hydra.service.tree;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface ServiceNodeMetadata extends Pinenut {
    long getEnumId();
    void setEnumId(long id);

    GUID getGuid();
    void setGuid(GUID guid);

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

    String getServiceType();
    void setServiceType(String serviceType);

    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);
}