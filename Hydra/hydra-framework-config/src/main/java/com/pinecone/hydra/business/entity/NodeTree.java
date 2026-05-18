package com.pinecone.hydra.business.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface NodeTree extends Pinenut {

    long getEnumId();

    void setEnumId( long nEnumId );

    GUID getGuid();

    void setGuid( GUID guid );

    GUID getParentGuid();

    void setParentGuid( GUID parentGuid );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );
}
