package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;

import java.time.LocalDateTime;

public class GenericServiceInstanceEntity implements ServiceInstanceEntry {
    protected Long mId;

    protected GUID mGuid;

    protected GUID mServiceGuid;

    protected String mszStatus;

    protected Long mClientId;

    protected String mszTransportType;

    protected String mszRemoteAddress;

    protected String mszEndpointProtocol;

    protected String mszEndpointHost;

    protected Integer mEndpointPort;

    protected String mszEndpointPath;

    protected String mszEndpointAddress;

    protected String mszStatusReason;

    protected String mszVersion;

    protected String mszZone;

    protected int mnWeight = 100;

    protected LocalDateTime mRegisterTime;

    protected LocalDateTime mLastHeartbeatTime;

    protected LocalDateTime mExpireTime;

    protected LocalDateTime mOfflineTime;

    protected String mszMetadataJson;

    protected LocalDateTime mLatestStartTime;

    protected LocalDateTime mLatestEndTime;

    protected String mErrorCause;

    protected int mnConnectionCount;

    protected GUID mDeployGuid;

    protected String mIp;

    @Override
    public Long getId() {
        return this.mId;
    }

    @Override
    public void setId( Long id ) {
        this.mId = id;
    }

    @Override
    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    @Override
    public void setServiceGuid( GUID guid ) {
        this.mServiceGuid = guid;
    }

    @Override
    public GUID getServiceGuid() {
        return this.mServiceGuid;
    }

    @Override
    public String getStatus(){
        return this.mszStatus;
    }

    @Override
    public void setStatus( String status ){
        this.mszStatus = status;
    }

    @Override
    public Long getClientId() {
        return this.mClientId;
    }

    @Override
    public void setClientId( Long clientId ) {
        this.mClientId = clientId;
    }

    @Override
    public String getTransportType() {
        return this.mszTransportType;
    }

    @Override
    public void setTransportType( String transportType ) {
        this.mszTransportType = transportType;
    }

    @Override
    public String getRemoteAddress() {
        return this.mszRemoteAddress;
    }

    @Override
    public void setRemoteAddress( String remoteAddress ) {
        this.mszRemoteAddress = remoteAddress;
    }

    @Override
    public String getEndpointProtocol() {
        return this.mszEndpointProtocol;
    }

    @Override
    public void setEndpointProtocol( String endpointProtocol ) {
        this.mszEndpointProtocol = endpointProtocol;
    }

    @Override
    public String getEndpointHost() {
        return this.mszEndpointHost;
    }

    @Override
    public void setEndpointHost( String endpointHost ) {
        this.mszEndpointHost = endpointHost;
    }

    @Override
    public Integer getEndpointPort() {
        return this.mEndpointPort;
    }

    @Override
    public void setEndpointPort( Integer endpointPort ) {
        this.mEndpointPort = endpointPort;
    }

    @Override
    public String getEndpointPath() {
        return this.mszEndpointPath;
    }

    @Override
    public void setEndpointPath( String endpointPath ) {
        this.mszEndpointPath = endpointPath;
    }

    @Override
    public String getEndpointAddress() {
        return this.mszEndpointAddress;
    }

    @Override
    public void setEndpointAddress( String endpointAddress ) {
        this.mszEndpointAddress = endpointAddress;
    }

    @Override
    public String getStatusReason() {
        return this.mszStatusReason;
    }

    @Override
    public void setStatusReason( String statusReason ) {
        this.mszStatusReason = statusReason;
    }

    @Override
    public String getVersion() {
        return this.mszVersion;
    }

    @Override
    public void setVersion( String version ) {
        this.mszVersion = version;
    }

    @Override
    public String getZone() {
        return this.mszZone;
    }

    @Override
    public void setZone( String zone ) {
        this.mszZone = zone;
    }

    @Override
    public int getWeight() {
        return this.mnWeight;
    }

    @Override
    public void setWeight( int weight ) {
        this.mnWeight = weight;
    }

    @Override
    public LocalDateTime getRegisterTime() {
        return this.mRegisterTime;
    }

    @Override
    public void setRegisterTime( LocalDateTime registerTime ) {
        this.mRegisterTime = registerTime;
    }

    @Override
    public LocalDateTime getLastHeartbeatTime() {
        return this.mLastHeartbeatTime;
    }

    @Override
    public void setLastHeartbeatTime( LocalDateTime lastHeartbeatTime ) {
        this.mLastHeartbeatTime = lastHeartbeatTime;
    }

    @Override
    public LocalDateTime getExpireTime() {
        return this.mExpireTime;
    }

    @Override
    public void setExpireTime( LocalDateTime expireTime ) {
        this.mExpireTime = expireTime;
    }

    @Override
    public LocalDateTime getOfflineTime() {
        return this.mOfflineTime;
    }

    @Override
    public void setOfflineTime( LocalDateTime offlineTime ) {
        this.mOfflineTime = offlineTime;
    }

    @Override
    public String getMetadataJson() {
        return this.mszMetadataJson;
    }

    @Override
    public void setMetadataJson( String metadataJson ) {
        this.mszMetadataJson = metadataJson;
    }

    @Override
    public LocalDateTime getLatestStartTime(){
        return this.mLatestStartTime;
    }

    @Override
    public void setLatestStartTime( LocalDateTime latestStartTime ){
        this.mLatestStartTime = latestStartTime;
    }

    @Override
    public LocalDateTime getLatestEndTime(){
        return this.mLatestEndTime;
    }

    @Override
    public void setLatestEndTime( LocalDateTime latestEndTime ){
        this.mLatestEndTime = latestEndTime;
    }

    @Override
    public String getErrorCause(){
        if ( this.mszStatusReason != null ) {
            return this.mszStatusReason;
        }
        return this.mErrorCause;
    }

    @Override
    public void setErrorCause( String errorCause ){
        this.mErrorCause = errorCause;
        this.mszStatusReason = errorCause;
    }

    @Override
    public int getConnectionCount(){
        return this.mnConnectionCount;
    }

    @Override
    public void setConnectionCount( int connectionCount ){
        this.mnConnectionCount = connectionCount;
    }

    @Override
    public GUID getDeployGuid() {
        return this.mDeployGuid;
    }

    @Override
    public void setDeployGuid( GUID deployGuid ) {
        this.mDeployGuid = deployGuid;
    }

    @Override
    public String getIp() {
        if ( this.mszEndpointHost != null ) {
            return this.mszEndpointHost;
        }
        return this.mIp;
    }

    @Override
    public void setIp( String ip ) {
        this.mIp = ip;
        this.mszEndpointHost = ip;
    }
}
