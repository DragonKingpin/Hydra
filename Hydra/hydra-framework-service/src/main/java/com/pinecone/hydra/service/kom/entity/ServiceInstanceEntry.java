package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface ServiceInstanceEntry extends Pinenut {
    void setGuid( GUID guid );

    GUID getGuid();

    void setServiceGuid( GUID guid );

    GUID getServiceGuid();

    int getStatus();

    void setStatus( int status );

    LocalDateTime getLatestStartTime();


    void setLatestStartTime( LocalDateTime latestStartTime );

    LocalDateTime getLatestEndTime();

    void setLatestEndTime( LocalDateTime latestEndTime );

    String getErrorCause();

    void setErrorCause( String errorCause );

    int getRunCount();

    void setRunCount( int runCount );

    GUID getDeployGuid();

    void setDeployGuid( GUID deployGuid );

    String getIp();

    void setIp( String ip );
}
