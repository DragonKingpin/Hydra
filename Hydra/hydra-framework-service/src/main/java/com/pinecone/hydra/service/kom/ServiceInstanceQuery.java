package com.pinecone.hydra.service.kom;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class ServiceInstanceQuery implements Pinenut {

    public static final long DEFAULT_LIMIT = 20;

    public static final long MAX_LIMIT = 1000;

    protected long mOffset = 0;

    protected long mLimit = DEFAULT_LIMIT;

    protected String mszKeyword;

    protected GUID mInstanceGuid;

    protected GUID mServiceGuid;

    protected String mszStatus;

    protected GUID mDeployGuid;

    protected String mszIp;

    protected LocalDateTime mLatestStartTimeStart;

    protected LocalDateTime mLatestStartTimeEnd;

    protected LocalDateTime mLatestEndTimeStart;

    protected LocalDateTime mLatestEndTimeEnd;

    public long getOffset() {
        return this.mOffset;
    }

    public void setOffset( long nOffset ) {
        this.mOffset = Math.max( 0, nOffset );
    }

    public long getLimit() {
        return this.mLimit;
    }

    public void setLimit( long nLimit ) {
        if ( nLimit <= 0 ) {
            this.mLimit = DEFAULT_LIMIT;
            return;
        }
        this.mLimit = Math.min( nLimit, MAX_LIMIT );
    }

    public String getKeyword() {
        return this.mszKeyword;
    }

    public void setKeyword( String szKeyword ) {
        this.mszKeyword = szKeyword;
    }

    public GUID getInstanceGuid() {
        return this.mInstanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.mInstanceGuid = instanceGuid;
    }

    public GUID getServiceGuid() {
        return this.mServiceGuid;
    }

    public void setServiceGuid( GUID serviceGuid ) {
        this.mServiceGuid = serviceGuid;
    }

    public String getStatus() {
        return this.mszStatus;
    }

    public void setStatus( String szStatus ) {
        this.mszStatus = szStatus;
    }

    public GUID getDeployGuid() {
        return this.mDeployGuid;
    }

    public void setDeployGuid( GUID deployGuid ) {
        this.mDeployGuid = deployGuid;
    }

    public String getIp() {
        return this.mszIp;
    }

    public void setIp( String szIp ) {
        this.mszIp = szIp;
    }

    public LocalDateTime getLatestStartTimeStart() {
        return this.mLatestStartTimeStart;
    }

    public void setLatestStartTimeStart( LocalDateTime latestStartTimeStart ) {
        this.mLatestStartTimeStart = latestStartTimeStart;
    }

    public LocalDateTime getLatestStartTimeEnd() {
        return this.mLatestStartTimeEnd;
    }

    public void setLatestStartTimeEnd( LocalDateTime latestStartTimeEnd ) {
        this.mLatestStartTimeEnd = latestStartTimeEnd;
    }

    public LocalDateTime getLatestEndTimeStart() {
        return this.mLatestEndTimeStart;
    }

    public void setLatestEndTimeStart( LocalDateTime latestEndTimeStart ) {
        this.mLatestEndTimeStart = latestEndTimeStart;
    }

    public LocalDateTime getLatestEndTimeEnd() {
        return this.mLatestEndTimeEnd;
    }

    public void setLatestEndTimeEnd( LocalDateTime latestEndTimeEnd ) {
        this.mLatestEndTimeEnd = latestEndTimeEnd;
    }
}
