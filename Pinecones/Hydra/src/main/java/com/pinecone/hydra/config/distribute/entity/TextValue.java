package com.pinecone.hydra.config.distribute.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface TextValue {
    int getEnumId();
    void setEnumId(int enumId);
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
