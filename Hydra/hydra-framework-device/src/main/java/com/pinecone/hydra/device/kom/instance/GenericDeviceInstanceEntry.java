package com.pinecone.hydra.device.kom.instance;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.device.DeviceStatus;

import java.time.LocalDateTime;

public class GenericDeviceInstanceEntry implements DeviceInstanceEntry {

    protected GUID instanceGuid;

    protected GUID deviceGuid;

    protected long clientId;

    protected String connectionId;

    protected GUID sessionGuid;

    protected String transportType;

    protected String remoteAddress;

    protected String endpointProtocol;

    protected String endpointHost;

    protected Integer endpointPort;

    protected String endpointPath;

    protected String endpointAddress;

    protected DeviceStatus status;

    protected String statusReason;

    protected LocalDateTime registerTime;

    protected LocalDateTime lastHeartbeatTime;

    protected LocalDateTime expireTime;

    protected LocalDateTime offlineTime;

    protected LocalDateTime deregisterTime;

    protected int connectionCount;

    protected LocalDateTime latestStartTime;

    protected LocalDateTime latestEndTime;

    protected String metadataJson;

    @Override
    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    @Override
    public void setInstanceGuid( GUID instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    @Override
    public GUID getDeviceGuid() {
        return this.deviceGuid;
    }

    @Override
    public void setDeviceGuid( GUID deviceGuid ) {
        this.deviceGuid = deviceGuid;
    }

    @Override
    public long getClientId() {
        return this.clientId;
    }

    @Override
    public void setClientId( long clientId ) {
        this.clientId = clientId;
    }

    @Override
    public String getConnectionId() {
        return this.connectionId;
    }

    @Override
    public void setConnectionId( String connectionId ) {
        this.connectionId = connectionId;
    }

    @Override
    public GUID getSessionGuid() {
        return this.sessionGuid;
    }

    @Override
    public void setSessionGuid( GUID sessionGuid ) {
        this.sessionGuid = sessionGuid;
    }

    @Override
    public String getTransportType() {
        return this.transportType;
    }

    @Override
    public void setTransportType( String transportType ) {
        this.transportType = transportType;
    }

    @Override
    public String getRemoteAddress() {
        return this.remoteAddress;
    }

    @Override
    public void setRemoteAddress( String remoteAddress ) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    public String getEndpointProtocol() {
        return this.endpointProtocol;
    }

    @Override
    public void setEndpointProtocol( String endpointProtocol ) {
        this.endpointProtocol = endpointProtocol;
    }

    @Override
    public String getEndpointHost() {
        return this.endpointHost;
    }

    @Override
    public void setEndpointHost( String endpointHost ) {
        this.endpointHost = endpointHost;
    }

    @Override
    public Integer getEndpointPort() {
        return this.endpointPort;
    }

    @Override
    public void setEndpointPort( Integer endpointPort ) {
        this.endpointPort = endpointPort;
    }

    @Override
    public String getEndpointPath() {
        return this.endpointPath;
    }

    @Override
    public void setEndpointPath( String endpointPath ) {
        this.endpointPath = endpointPath;
    }

    @Override
    public String getEndpointAddress() {
        return this.endpointAddress;
    }

    @Override
    public void setEndpointAddress( String endpointAddress ) {
        this.endpointAddress = endpointAddress;
    }

    @Override
    public DeviceStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus( DeviceStatus status ) {
        this.status = status;
    }

    @Override
    public String getStatusReason() {
        return this.statusReason;
    }

    @Override
    public void setStatusReason( String statusReason ) {
        this.statusReason = statusReason;
    }

    @Override
    public LocalDateTime getRegisterTime() {
        return this.registerTime;
    }

    @Override
    public void setRegisterTime( LocalDateTime registerTime ) {
        this.registerTime = registerTime;
    }

    @Override
    public LocalDateTime getLastHeartbeatTime() {
        return this.lastHeartbeatTime;
    }

    @Override
    public void setLastHeartbeatTime( LocalDateTime lastHeartbeatTime ) {
        this.lastHeartbeatTime = lastHeartbeatTime;
    }

    @Override
    public LocalDateTime getExpireTime() {
        return this.expireTime;
    }

    @Override
    public void setExpireTime( LocalDateTime expireTime ) {
        this.expireTime = expireTime;
    }

    @Override
    public LocalDateTime getOfflineTime() {
        return this.offlineTime;
    }

    @Override
    public void setOfflineTime( LocalDateTime offlineTime ) {
        this.offlineTime = offlineTime;
    }

    @Override
    public LocalDateTime getDeregisterTime() {
        return this.deregisterTime;
    }

    @Override
    public void setDeregisterTime( LocalDateTime deregisterTime ) {
        this.deregisterTime = deregisterTime;
    }

    @Override
    public int getConnectionCount() {
        return this.connectionCount;
    }

    @Override
    public void setConnectionCount( int connectionCount ) {
        this.connectionCount = connectionCount;
    }

    @Override
    public LocalDateTime getLatestStartTime() {
        return this.latestStartTime;
    }

    @Override
    public void setLatestStartTime( LocalDateTime latestStartTime ) {
        this.latestStartTime = latestStartTime;
    }

    @Override
    public LocalDateTime getLatestEndTime() {
        return this.latestEndTime;
    }

    @Override
    public void setLatestEndTime( LocalDateTime latestEndTime ) {
        this.latestEndTime = latestEndTime;
    }

    @Override
    public String getMetadataJson() {
        return this.metadataJson;
    }

    @Override
    public void setMetadataJson( String metadataJson ) {
        this.metadataJson = metadataJson;
    }
}
