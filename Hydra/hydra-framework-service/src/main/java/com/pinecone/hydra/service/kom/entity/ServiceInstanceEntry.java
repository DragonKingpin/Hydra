package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public interface ServiceInstanceEntry extends Pinenut {
    Long getId();

    void setId( Long id );

    void setGuid( GUID guid );

    GUID getGuid();

    void setServiceGuid( GUID guid );

    GUID getServiceGuid();

    String getStatus();

    void setStatus( String status );

    Long getClientId();

    void setClientId( Long clientId );

    String getTransportType();

    void setTransportType( String transportType );

    String getRemoteAddress();

    void setRemoteAddress( String remoteAddress );

    String getEndpointProtocol();

    void setEndpointProtocol( String endpointProtocol );

    String getEndpointHost();

    void setEndpointHost( String endpointHost );

    Integer getEndpointPort();

    void setEndpointPort( Integer endpointPort );

    String getEndpointPath();

    void setEndpointPath( String endpointPath );

    String getEndpointAddress();

    void setEndpointAddress( String endpointAddress );

    String getStatusReason();

    void setStatusReason( String statusReason );

    String getVersion();

    void setVersion( String version );

    String getZone();

    void setZone( String zone );

    int getWeight();

    void setWeight( int weight );

    LocalDateTime getRegisterTime();

    void setRegisterTime( LocalDateTime registerTime );

    LocalDateTime getLastHeartbeatTime();

    void setLastHeartbeatTime( LocalDateTime lastHeartbeatTime );

    LocalDateTime getExpireTime();

    void setExpireTime( LocalDateTime expireTime );

    LocalDateTime getOfflineTime();

    void setOfflineTime( LocalDateTime offlineTime );

    String getMetadataJson();

    void setMetadataJson( String metadataJson );

    LocalDateTime getLatestStartTime();


    void setLatestStartTime( LocalDateTime latestStartTime );

    LocalDateTime getLatestEndTime();

    void setLatestEndTime( LocalDateTime latestEndTime );

    String getErrorCause();

    void setErrorCause( String errorCause );

    int getConnectionCount();

    void setConnectionCount( int connectionCount );

    @Deprecated
    default int getRunCount() {
        return this.getConnectionCount();
    }

    @Deprecated
    default void setRunCount( int runCount ) {
        this.setConnectionCount( runCount );
    }

    GUID getDeployGuid();

    void setDeployGuid( GUID deployGuid );

    GUID getRuntimeNodeGuid();

    void setRuntimeNodeGuid( GUID runtimeNodeGuid );

    String getRuntimeNodeId();

    void setRuntimeNodeId( String runtimeNodeId );

    String getIp();

    void setIp( String ip );
}
