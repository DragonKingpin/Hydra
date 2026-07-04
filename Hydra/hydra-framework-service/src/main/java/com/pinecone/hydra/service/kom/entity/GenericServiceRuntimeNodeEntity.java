package com.pinecone.hydra.service.kom.entity;

import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class GenericServiceRuntimeNodeEntity implements ServiceRuntimeNodeEntry {

    protected Long mId;

    protected GUID mGuid;

    protected GUID mServiceGuid;

    protected String mszNodeId;

    protected String mszAlias;

    protected String mszStatus;

    protected String mszVersion;

    protected String mszZone;

    protected String mszMetadataJson;

    protected GUID mLatestInstanceGuid;

    protected LocalDateTime mLatestStartTime;

    protected LocalDateTime mLatestEndTime;

    protected LocalDateTime mLastHeartbeatTime;

    protected LocalDateTime mCreateTime;

    protected LocalDateTime mUpdateTime;

    @Override
    public Long getId() {
        return this.mId;
    }

    @Override
    public void setId( Long id ) {
        this.mId = id;
    }

    @Override
    public GUID getGuid() {
        return this.mGuid;
    }

    @Override
    public void setGuid( GUID guid ) {
        this.mGuid = guid;
    }

    @Override
    public GUID getServiceGuid() {
        return this.mServiceGuid;
    }

    @Override
    public void setServiceGuid( GUID serviceGuid ) {
        this.mServiceGuid = serviceGuid;
    }

    @Override
    public String getNodeId() {
        return this.mszNodeId;
    }

    @Override
    public void setNodeId( String nodeId ) {
        this.mszNodeId = nodeId;
    }

    @Override
    public String getAlias() {
        return this.mszAlias;
    }

    @Override
    public void setAlias( String alias ) {
        this.mszAlias = alias;
    }

    @Override
    public String getStatus() {
        return this.mszStatus;
    }

    @Override
    public void setStatus( String status ) {
        this.mszStatus = status;
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
    public String getMetadataJson() {
        return this.mszMetadataJson;
    }

    @Override
    public void setMetadataJson( String metadataJson ) {
        this.mszMetadataJson = metadataJson;
    }

    @Override
    public GUID getLatestInstanceGuid() {
        return this.mLatestInstanceGuid;
    }

    @Override
    public void setLatestInstanceGuid( GUID latestInstanceGuid ) {
        this.mLatestInstanceGuid = latestInstanceGuid;
    }

    @Override
    public LocalDateTime getLatestStartTime() {
        return this.mLatestStartTime;
    }

    @Override
    public void setLatestStartTime( LocalDateTime latestStartTime ) {
        this.mLatestStartTime = latestStartTime;
    }

    @Override
    public LocalDateTime getLatestEndTime() {
        return this.mLatestEndTime;
    }

    @Override
    public void setLatestEndTime( LocalDateTime latestEndTime ) {
        this.mLatestEndTime = latestEndTime;
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
    public LocalDateTime getCreateTime() {
        return this.mCreateTime;
    }

    @Override
    public void setCreateTime( LocalDateTime createTime ) {
        this.mCreateTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return this.mUpdateTime;
    }

    @Override
    public void setUpdateTime( LocalDateTime updateTime ) {
        this.mUpdateTime = updateTime;
    }
}
