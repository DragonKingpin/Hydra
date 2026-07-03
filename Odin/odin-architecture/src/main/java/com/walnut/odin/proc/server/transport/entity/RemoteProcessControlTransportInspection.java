package com.walnut.odin.proc.server.transport.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportType;

public class RemoteProcessControlTransportInspection implements Pinenut {

    protected RemoteProcessControlTransportType mTransportType;

    protected boolean                           mbStarted;

    protected boolean                           mbTerminated;

    protected Object                            mRouteSource;

    protected Object                            mEndpointSource;

    protected int                               mnConnectedClientCount;

    protected int                               mnRegisteredControllerCount;

    protected int                               mnCompiledIfaceCount;

    public RemoteProcessControlTransportType getTransportType() {
        return this.mTransportType;
    }

    public void setTransportType( RemoteProcessControlTransportType transportType ) {
        this.mTransportType = transportType;
    }

    public boolean isStarted() {
        return this.mbStarted;
    }

    public void setStarted( boolean bStarted ) {
        this.mbStarted = bStarted;
    }

    public boolean isTerminated() {
        return this.mbTerminated;
    }

    public void setTerminated( boolean bTerminated ) {
        this.mbTerminated = bTerminated;
    }

    public Object getRouteSource() {
        return this.mRouteSource;
    }

    public void setRouteSource( Object routeSource ) {
        this.mRouteSource = routeSource;
    }

    public Object getEndpointSource() {
        return this.mEndpointSource;
    }

    public void setEndpointSource( Object endpointSource ) {
        this.mEndpointSource = endpointSource;
    }

    public int getConnectedClientCount() {
        return this.mnConnectedClientCount;
    }

    public void setConnectedClientCount( int nConnectedClientCount ) {
        this.mnConnectedClientCount = nConnectedClientCount;
    }

    public int getRegisteredControllerCount() {
        return this.mnRegisteredControllerCount;
    }

    public void setRegisteredControllerCount( int nRegisteredControllerCount ) {
        this.mnRegisteredControllerCount = nRegisteredControllerCount;
    }

    public int getCompiledIfaceCount() {
        return this.mnCompiledIfaceCount;
    }

    public void setCompiledIfaceCount( int nCompiledIfaceCount ) {
        this.mnCompiledIfaceCount = nCompiledIfaceCount;
    }
}
