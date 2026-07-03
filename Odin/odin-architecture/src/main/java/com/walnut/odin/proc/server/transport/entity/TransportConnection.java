package com.walnut.odin.proc.server.transport.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class TransportConnection implements Pinenut {

    protected String  mszType;

    protected String  mszIdentity;

    protected String  mszRemoteAddress;

    protected String  mszLocalAddress;

    protected String  mszStatus;

    protected boolean mbActive;

    protected Long    mnLastActiveTimeMillis;

    protected Long    mnLastHeartbeatTimeMillis;

    public String getType() {
        return this.mszType;
    }

    public void setType( String szType ) {
        this.mszType = szType;
    }

    public String getIdentity() {
        return this.mszIdentity;
    }

    public void setIdentity( String szIdentity ) {
        this.mszIdentity = szIdentity;
    }

    public String getRemoteAddress() {
        return this.mszRemoteAddress;
    }

    public void setRemoteAddress( String szRemoteAddress ) {
        this.mszRemoteAddress = szRemoteAddress;
    }

    public String getLocalAddress() {
        return this.mszLocalAddress;
    }

    public void setLocalAddress( String szLocalAddress ) {
        this.mszLocalAddress = szLocalAddress;
    }

    public String getStatus() {
        return this.mszStatus;
    }

    public void setStatus( String szStatus ) {
        this.mszStatus = szStatus;
    }

    public boolean isActive() {
        return this.mbActive;
    }

    public void setActive( boolean bActive ) {
        this.mbActive = bActive;
    }

    public Long getLastActiveTimeMillis() {
        return this.mnLastActiveTimeMillis;
    }

    public void setLastActiveTimeMillis( Long nLastActiveTimeMillis ) {
        this.mnLastActiveTimeMillis = nLastActiveTimeMillis;
    }

    public Long getLastHeartbeatTimeMillis() {
        return this.mnLastHeartbeatTimeMillis;
    }

    public void setLastHeartbeatTimeMillis( Long nLastHeartbeatTimeMillis ) {
        this.mnLastHeartbeatTimeMillis = nLastHeartbeatTimeMillis;
    }
}
