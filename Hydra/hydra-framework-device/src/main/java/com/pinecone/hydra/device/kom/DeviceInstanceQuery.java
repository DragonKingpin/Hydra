package com.pinecone.hydra.device.kom;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.time.LocalDateTime;

public class DeviceInstanceQuery implements Pinenut {

    public static final long DEFAULT_LIMIT = 20;

    public static final long MAX_LIMIT = 1000;

    protected long offset;

    protected long limit = DEFAULT_LIMIT;

    protected String keyword;

    protected GUID instanceGuid;

    protected GUID deviceGuid;

    protected Long clientId;

    protected String status;

    protected String transportType;

    protected LocalDateTime latestStartTimeStart;

    protected LocalDateTime latestStartTimeEnd;

    protected LocalDateTime latestEndTimeStart;

    protected LocalDateTime latestEndTimeEnd;

    public long getOffset() {
        return this.offset;
    }

    public void setOffset( long offset ) {
        this.offset = Math.max( 0, offset );
    }

    public long getLimit() {
        return this.limit;
    }

    public void setLimit( long limit ) {
        if ( limit <= 0 ) {
            this.limit = DEFAULT_LIMIT;
            return;
        }
        this.limit = Math.min( limit, MAX_LIMIT );
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword( String keyword ) {
        this.keyword = keyword;
    }

    public GUID getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( GUID instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    public GUID getDeviceGuid() {
        return this.deviceGuid;
    }

    public void setDeviceGuid( GUID deviceGuid ) {
        this.deviceGuid = deviceGuid;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId( Long clientId ) {
        this.clientId = clientId;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public String getTransportType() {
        return this.transportType;
    }

    public void setTransportType( String transportType ) {
        this.transportType = transportType;
    }

    public LocalDateTime getLatestStartTimeStart() {
        return this.latestStartTimeStart;
    }

    public void setLatestStartTimeStart( LocalDateTime latestStartTimeStart ) {
        this.latestStartTimeStart = latestStartTimeStart;
    }

    public LocalDateTime getLatestStartTimeEnd() {
        return this.latestStartTimeEnd;
    }

    public void setLatestStartTimeEnd( LocalDateTime latestStartTimeEnd ) {
        this.latestStartTimeEnd = latestStartTimeEnd;
    }

    public LocalDateTime getLatestEndTimeStart() {
        return this.latestEndTimeStart;
    }

    public void setLatestEndTimeStart( LocalDateTime latestEndTimeStart ) {
        this.latestEndTimeStart = latestEndTimeStart;
    }

    public LocalDateTime getLatestEndTimeEnd() {
        return this.latestEndTimeEnd;
    }

    public void setLatestEndTimeEnd( LocalDateTime latestEndTimeEnd ) {
        this.latestEndTimeEnd = latestEndTimeEnd;
    }
}
