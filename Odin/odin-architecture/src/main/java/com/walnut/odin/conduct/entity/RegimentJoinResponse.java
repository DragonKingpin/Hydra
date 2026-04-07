package com.walnut.odin.conduct.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public class RegimentJoinResponse implements Pinenut {

    protected String mszGuid;
    protected String mszName;
    protected String mszClusterPath;
    protected String mszClusterName;
    protected long   mnControlClientId;
    protected int    mnPriority;

    protected String mszQueueName;
    protected int    mnQueueMaxCapacity;
    protected int    mnQueueMinCapacity;
    protected int    mnQueueRuntimeInstanceCapacity;

    protected String mszErrorMsg;

    public RegimentJoinResponse() {
    }


    public String getGuid() {
        return this.mszGuid;
    }

    public void setGuid( String szGuid ) {
        this.mszGuid = szGuid;
    }

    public String getName() {
        return this.mszName;
    }

    public void setName( String szName ) {
        this.mszName = szName;
    }

    public String getClusterPath() {
        return this.mszClusterPath;
    }

    public void setClusterPath( String szClusterPath ) {
        this.mszClusterPath = szClusterPath;
    }

    public String getClusterName() {
        return this.mszClusterName;
    }

    public void setClusterName( String szClusterName ) {
        this.mszClusterName = szClusterName;
    }

    public long getControlClientId() {
        return this.mnControlClientId;
    }

    public void setControlClientId( long nControlClientId ) {
        this.mnControlClientId = nControlClientId;
    }

    public int getPriority() {
        return this.mnPriority;
    }

    public void setPriority( int nPriority ) {
        this.mnPriority = nPriority;
    }

    public String getQueueName() {
        return this.mszQueueName;
    }

    public void setQueueName( String szQueueName ) {
        this.mszQueueName = szQueueName;
    }

    public int getQueueMaxCapacity() {
        return this.mnQueueMaxCapacity;
    }

    public void setQueueMaxCapacity( int nQueueMaxCapacity ) {
        this.mnQueueMaxCapacity = nQueueMaxCapacity;
    }

    public int getQueueMinCapacity() {
        return this.mnQueueMinCapacity;
    }

    public void setQueueMinCapacity( int nQueueMinCapacity ) {
        this.mnQueueMinCapacity = nQueueMinCapacity;
    }

    public int getQueueRuntimeInstanceCapacity() {
        return this.mnQueueRuntimeInstanceCapacity;
    }

    public void setQueueRuntimeInstanceCapacity( int nQueueRuntimeInstanceCapacity ) {
        this.mnQueueRuntimeInstanceCapacity = nQueueRuntimeInstanceCapacity;
    }

    public String getErrorMsg() {
        return this.mszErrorMsg;
    }

    public void setErrorMsg( String errorMsg ) {
        this.mszErrorMsg = errorMsg;
    }
}
