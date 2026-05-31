package com.pinecone.hydra.service.registry.server.transport.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransport;

public class ServiceTransportHandle implements Pinenut {

    protected long                    mnClientId;

    protected ServiceControlTransport mTransport;

    protected long                    mnRegisterTimeMillis;

    public long getClientId() {
        return this.mnClientId;
    }

    public void setClientId( long nClientId ) {
        this.mnClientId = nClientId;
    }

    public ServiceControlTransport getTransport() {
        return this.mTransport;
    }

    public void setTransport( ServiceControlTransport transport ) {
        this.mTransport = transport;
    }

    public long getRegisterTimeMillis() {
        return this.mnRegisterTimeMillis;
    }

    public void setRegisterTimeMillis( long nRegisterTimeMillis ) {
        this.mnRegisterTimeMillis = nRegisterTimeMillis;
    }

}
