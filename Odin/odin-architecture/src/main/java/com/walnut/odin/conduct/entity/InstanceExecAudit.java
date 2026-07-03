package com.walnut.odin.conduct.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface InstanceExecAudit extends Pinenut {

    long getId();

    void setId( long id );

    GUID getTaskGuid();

    void setTaskGuid( GUID taskGuid );

    GUID getInstanceGuid();

    void setInstanceGuid( GUID instanceGuid );

    int getSequenceCnt();

    void setSequenceCnt( int sequenceCnt );

    int getCurrentRetryNumber();

    void setCurrentRetryNumber( int currentRetryNumber );

    String getAuditState();

    void setAuditState( String auditState );

    String getAuditType();

    void setAuditType( String auditType );

    String getMessage();

    void setMessage( String message );

    String getArtifactUri();

    void setArtifactUri( String artifactUri );

    String getArtifactDigest();

    void setArtifactDigest( String artifactDigest );

    String getPayload();

    void setPayload( String payload );

    LocalDateTime getStartTime();

    void setStartTime( LocalDateTime startTime );

    LocalDateTime getFinishTime();

    void setFinishTime( LocalDateTime finishTime );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );
}
