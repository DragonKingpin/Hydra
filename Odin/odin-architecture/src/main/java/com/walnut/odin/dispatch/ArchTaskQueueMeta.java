package com.walnut.odin.dispatch;
import com.pinecone.hydra.deploy.Server;

public abstract class ArchTaskQueueMeta implements TaskQueueMeta {

    protected String mszName;

    protected Server mServer;

    protected String mszClusterPath;

    protected String mszClusterName;

    protected int mnControlClientId;

    protected int mnCapacity;

    protected int mnMaxCapacity;

    protected int mnMinCapacity;

    protected int mnUsedCapacity;

    protected int mnRuntimeInstanceCapacity;


    @Override
    public String getName() {
        return this.mszName;
    }

    @Override
    public Server getDeployClusterServer() {
        return this.mServer;
    }

    @Override
    public String getClusterPath() {
        return this.mszClusterPath;
    }

    @Override
    public String getClusterName() {
        return this.mszClusterName;
    }

    @Override
    public int getControlClientId() {
        return this.mnControlClientId;
    }

    @Override
    public int getCapacity() {
        return this.mnCapacity;
    }

    @Override
    public int getMaxCapacity() {
        return this.mnMaxCapacity;
    }

    @Override
    public int getMinCapacity() {
        return this.mnMinCapacity;
    }

    @Override
    public int getUsedCapacity() {
        return this.mnUsedCapacity;
    }

    @Override
    public int getRuntimeInstanceCapacity() {
        return this.mnRuntimeInstanceCapacity;
    }

    protected void setName( String szName ) {
        this.mszName = szName;
    }

    protected void setDeployClusterServer( Server server ) {
        this.mServer = server;
    }

    protected void setClusterPath( String szClusterPath ) {
        this.mszClusterPath = szClusterPath;
    }

    protected void setClusterName( String szClusterName ) {
        this.mszClusterName = szClusterName;
    }

    protected void setControlClientId( int nControlClientId ) {
        this.mnControlClientId = nControlClientId;
    }

    protected void setCapacity( int nCapacity ) {
        this.mnCapacity = nCapacity;
    }

    protected void setMaxCapacity( int nMaxCapacity ) {
        this.mnMaxCapacity = nMaxCapacity;
    }

    protected void setMinCapacity( int nMinCapacity ) {
        this.mnMinCapacity = nMinCapacity;
    }

    protected void setUsedCapacity( int nUsedCapacity ) {
        this.mnUsedCapacity = nUsedCapacity;
    }

    protected void setRuntimeInstanceCapacity( int nRuntimeInstanceCapacity ) {
        this.mnRuntimeInstanceCapacity = nRuntimeInstanceCapacity;
    }

}