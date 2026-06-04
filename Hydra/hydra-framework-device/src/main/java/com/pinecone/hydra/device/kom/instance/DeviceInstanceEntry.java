package com.pinecone.hydra.device.kom.instance;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.DeviceStatus;

import java.time.LocalDateTime;

public interface DeviceInstanceEntry extends Pinenut {

    GUID getInstanceGuid();

    void setInstanceGuid( GUID instanceGuid );

    GUID getDeviceGuid();

    void setDeviceGuid( GUID deviceGuid );

    long getClientId();

    void setClientId( long clientId );

    String getConnectionId();

    void setConnectionId( String connectionId );

    GUID getSessionGuid();

    void setSessionGuid( GUID sessionGuid );

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

    DeviceStatus getStatus();

    void setStatus( DeviceStatus status );

    String getStatusReason();

    void setStatusReason( String statusReason );

    LocalDateTime getRegisterTime();

    void setRegisterTime( LocalDateTime registerTime );

    LocalDateTime getLastHeartbeatTime();

    void setLastHeartbeatTime( LocalDateTime lastHeartbeatTime );

    LocalDateTime getExpireTime();

    void setExpireTime( LocalDateTime expireTime );

    LocalDateTime getOfflineTime();

    void setOfflineTime( LocalDateTime offlineTime );

    LocalDateTime getDeregisterTime();

    void setDeregisterTime( LocalDateTime deregisterTime );

    int getConnectionCount();

    void setConnectionCount( int connectionCount );

    LocalDateTime getLatestStartTime();

    void setLatestStartTime( LocalDateTime latestStartTime );

    LocalDateTime getLatestEndTime();

    void setLatestEndTime( LocalDateTime latestEndTime );

    String getMetadataJson();

    void setMetadataJson( String metadataJson );
}
