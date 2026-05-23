package com.walnut.odin.proc.server.transport.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class TransportConnection implements Pinenut {

    protected String  mszType;

    protected String  mszIdentity;

    protected String  mszRemoteAddress;

    protected String  mszStatus;

    protected boolean mbActive;

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
}
