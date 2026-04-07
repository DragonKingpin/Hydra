package com.pinecone.hydra.deploy.kom.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface DeployInsMapping extends Pinenut {
    void setEnumId( long enumId );

    long getEnumId();

    void setDeployGuid( GUID deployGuid );

    GUID getDeployGuid();

    void setServiceInsGuid( GUID serviceInsGuid );

    GUID getServiceInsGuid();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getCreateTime();

    void setUpdateTime( LocalDateTime updateTime );

    LocalDateTime getUpdateTime();
}
