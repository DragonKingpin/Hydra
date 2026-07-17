package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface ServiceRuntimeNodeEntry extends Pinenut {

    Long getId();

    void setId( Long id );

    GUID getGuid();

    void setGuid( GUID guid );

    GUID getServiceGuid();

    void setServiceGuid( GUID serviceGuid );

    String getNodeId();

    void setNodeId( String nodeId );

    String getAlias();

    void setAlias( String alias );

    String getStatus();

    void setStatus( String status );

    String getVersion();

    void setVersion( String version );

    String getZone();

    void setZone( String zone );

    String getMetadataJson();

    void setMetadataJson( String metadataJson );

    GUID getLatestInstanceGuid();

    void setLatestInstanceGuid( GUID latestInstanceGuid );

    LocalDateTime getLatestStartTime();

    void setLatestStartTime( LocalDateTime latestStartTime );

    LocalDateTime getLatestEndTime();

    void setLatestEndTime( LocalDateTime latestEndTime );

    LocalDateTime getLastHeartbeatTime();

    void setLastHeartbeatTime( LocalDateTime lastHeartbeatTime );

    LocalDateTime getCreateTime();

    void setCreateTime( LocalDateTime createTime );

    LocalDateTime getUpdateTime();

    void setUpdateTime( LocalDateTime updateTime );
}
