package com.pinecone.hydra.service.registry.server.transport.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransportType;

public class ServiceControlTransportInspection implements Pinenut {

    protected ServiceControlTransportType mTransportType;

    protected boolean                     mbStarted;

    protected boolean                     mbTerminated;

    protected Object                      mRouteSource;

    protected Object                      mEndpointSource;

    protected int                         mnConnectedClientCount;

    protected int                         mnRegisteredControllerCount;

    protected int                         mnCompiledIfaceCount;

    public ServiceControlTransportType getTransportType() {
        return this.mTransportType;
    }

    public void setTransportType( ServiceControlTransportType transportType ) {
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
