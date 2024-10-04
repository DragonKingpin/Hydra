package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface TextValue extends Pinenut {
    long getEnumId();

    void setEnumId(long enumId);

    GUID getGuid();

    void setGuid(GUID guid);

    String getValue();

    void setValue(String value);

    LocalDateTime getCreateTime();

    void setCreateTime(LocalDateTime createTime);

    LocalDateTime getUpdateTime();

    void setUpdateTime(LocalDateTime updateTime);

    String getType();

    void setType(String type);



}
