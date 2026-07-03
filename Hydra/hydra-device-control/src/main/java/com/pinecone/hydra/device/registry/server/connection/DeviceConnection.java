package com.pinecone.hydra.device.registry.server.connection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public class DeviceConnection implements Pinenut {

    protected String connectionId;

    protected String transportType;

    protected String remoteAddress;

    protected long connectedTime;

    protected GUID sessionGuid;

    public String getConnectionId() {
        return this.connectionId;
    }

    public void setConnectionId( String connectionId ) {
        this.connectionId = connectionId;
    }

    public String getTransportType() {
        return this.transportType;
    }

    public void setTransportType( String transportType ) {
        this.transportType = transportType;
    }

    public String getRemoteAddress() {
        return this.remoteAddress;
    }

    public void setRemoteAddress( String remoteAddress ) {
        this.remoteAddress = remoteAddress;
    }

    public long getConnectedTime() {
        return this.connectedTime;
    }

    public void setConnectedTime( long connectedTime ) {
        this.connectedTime = connectedTime;
    }

    public GUID getSessionGuid() {
        return this.sessionGuid;
    }

    public void setSessionGuid( GUID sessionGuid ) {
        this.sessionGuid = sessionGuid;
    }
}
