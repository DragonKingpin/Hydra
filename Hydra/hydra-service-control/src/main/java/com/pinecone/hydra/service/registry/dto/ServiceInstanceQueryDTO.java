package com.pinecone.hydra.service.registry.dto;

import com.pinecone.framework.system.prototype.Pinenut;

public class ServiceInstanceQueryDTO implements Pinenut {

    private long offset;

    private long limit;

    private String keyword;

    private String instanceGuid;

    private String serviceGuid;

    private String status;

    private String deployGuid;

    private String ip;

    private String latestStartTimeStart;

    private String latestStartTimeEnd;

    private String latestEndTimeStart;

    private String latestEndTimeEnd;

    public long getOffset() {
        return this.offset;
    }

    public void setOffset( long offset ) {
        this.offset = offset;
    }

    public long getLimit() {
        return this.limit;
    }

    public void setLimit( long limit ) {
        this.limit = limit;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword( String keyword ) {
        this.keyword = keyword;
    }

    public String getInstanceGuid() {
        return this.instanceGuid;
    }

    public void setInstanceGuid( String instanceGuid ) {
        this.instanceGuid = instanceGuid;
    }

    public String getServiceGuid() {
        return this.serviceGuid;
    }

    public void setServiceGuid( String serviceGuid ) {
        this.serviceGuid = serviceGuid;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus( String status ) {
        this.status = status;
    }

    public String getDeployGuid() {
        return this.deployGuid;
    }

    public void setDeployGuid( String deployGuid ) {
        this.deployGuid = deployGuid;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp( String ip ) {
        this.ip = ip;
    }

    public String getLatestStartTimeStart() {
        return this.latestStartTimeStart;
    }

    public void setLatestStartTimeStart( String latestStartTimeStart ) {
        this.latestStartTimeStart = latestStartTimeStart;
    }

    public String getLatestStartTimeEnd() {
        return this.latestStartTimeEnd;
    }

    public void setLatestStartTimeEnd( String latestStartTimeEnd ) {
        this.latestStartTimeEnd = latestStartTimeEnd;
    }

    public String getLatestEndTimeStart() {
        return this.latestEndTimeStart;
    }

    public void setLatestEndTimeStart( String latestEndTimeStart ) {
        this.latestEndTimeStart = latestEndTimeStart;
    }

    public String getLatestEndTimeEnd() {
        return this.latestEndTimeEnd;
    }

    public void setLatestEndTimeEnd( String latestEndTimeEnd ) {
        this.latestEndTimeEnd = latestEndTimeEnd;
    }
}
