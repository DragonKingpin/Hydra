package com.pinecone.hydra.service.registry.dao;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;

import java.time.LocalDateTime;

public class ServiceInstanceDO implements ServiceInstanceEntry {
    protected GUID mGuid;

    protected GUID mServiceGuid;

    protected int mnStatus;

    protected LocalDateTime mLatestStartTime;

    protected LocalDateTime mLatestEndTime;

    protected String mErrorCause;

    protected int mnRunCount;

    protected GUID mDeployGuid;

    protected String mIp;

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
    public int getStatus(){
        return this.mnStatus;
    }

    @Override
    public void setStatus( int status ){
        this.mnStatus = status;
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
        return this.mErrorCause;
    }

    @Override
    public void setErrorCause( String errorCause ){
        this.mErrorCause = errorCause;
    }

    @Override
    public int getRunCount(){
        return this.mnRunCount;
    }

    @Override
    public void setRunCount( int runCount ){
        this.mnRunCount = runCount;
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
        return this.mIp;
    }

    @Override
    public void setIp( String ip ) {
        this.mIp = ip;
    }
}
