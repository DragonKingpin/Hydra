package com.pinecone.hydra.service.registry.dto;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;

public class ServiceInstanceMetaDTO implements Pinenut {

    private String guid;

    private String serviceGuid;

    private String status;

    private String latestStartTime;

    private String latestEndTime;

    private String errorCause;

    private int connectionCount;

    private String deployGuid;

    private String ip;

    public String getGuid() {
        return this.guid;
    }

    public void setGuid( String guid ) {
        this.guid = guid;
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

    public String getLatestStartTime() {
        return this.latestStartTime;
    }

    public void setLatestStartTime( String latestStartTime ) {
        this.latestStartTime = latestStartTime;
    }

    public String getLatestEndTime() {
        return this.latestEndTime;
    }

    public void setLatestEndTime( String latestEndTime ) {
        this.latestEndTime = latestEndTime;
    }

    public String getErrorCause() {
        return this.errorCause;
    }

    public void setErrorCause( String errorCause ) {
        this.errorCause = errorCause;
    }

    public int getConnectionCount() {
        return this.connectionCount;
    }

    public void setConnectionCount( int connectionCount ) {
        this.connectionCount = connectionCount;
    }

    @Deprecated
    public int getRunCount() {
        return this.connectionCount;
    }

    @Deprecated
    public void setRunCount( int runCount ) {
        this.connectionCount = runCount;
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

    public static ServiceInstanceMetaDTO from( ServiceInstanceEntry entry ) {
        ServiceInstanceMetaDTO dto = new ServiceInstanceMetaDTO();
        if ( entry.getGuid() != null ) {
            dto.setGuid( entry.getGuid().toString() );
        }
        if ( entry.getServiceGuid() != null ) {
            dto.setServiceGuid( entry.getServiceGuid().toString() );
        }
        if ( entry.getLatestStartTime() != null ) {
            dto.setLatestStartTime( entry.getLatestStartTime().toString() );
        }
        if ( entry.getLatestEndTime() != null ) {
            dto.setLatestEndTime( entry.getLatestEndTime().toString() );
        }
        if ( entry.getDeployGuid() != null ) {
            dto.setDeployGuid( entry.getDeployGuid().toString() );
        }

        dto.setStatus( entry.getStatus() );
        dto.setErrorCause( entry.getErrorCause() );
        dto.setConnectionCount( entry.getConnectionCount() );
        dto.setIp( entry.getIp() );
        return dto;
    }
}
