package com.walnut.odin.conduct.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface TaskInstanceOperationLog extends Pinenut {

    long getId();

    void setId( long id );

    GUID getGuid();

    void setGuid( GUID guid );

    GUID getTaskGuid();

    void setTaskGuid( GUID taskGuid );

    GUID getInstanceGuid();

    void setInstanceGuid( GUID instanceGuid );

    String getInstanceName();

    void setInstanceName( String instanceName );

    String getOperationType();

    void setOperationType( String operationType );

    String getOperationSource();

    void setOperationSource( String operationSource );

    String getUserIdentifier();

    void setUserIdentifier( String userIdentifier );

    String getUserName();

    void setUserName( String userName );

    String getMessage();

    void setMessage( String message );

    String getPayload();

    void setPayload( String payload );

    LocalDateTime getOperationTime();

    void setOperationTime( LocalDateTime operationTime );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );
}
