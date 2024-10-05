package com.pinecone.hydra.scenario.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface ScenarioCommonData extends Pinenut {
    long getEnumId();
    void setEnumId(long id);

    GUID getGuid();
    void setGuid(GUID guid);

    LocalDateTime getCreateTime();
    void setCreateTime(LocalDateTime localDateTime);

    LocalDateTime getUpdateTime();
    void setUpdateTime(LocalDateTime updateTime);
}
