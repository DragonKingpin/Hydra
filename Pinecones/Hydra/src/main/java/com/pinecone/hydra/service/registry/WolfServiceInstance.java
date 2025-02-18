package com.pinecone.hydra.service.registry;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.Service;
import com.pinecone.hydra.service.Servicium;
import com.pinecone.hydra.service.entity.BindUSII;
import com.pinecone.hydra.service.entity.USII;

public class WolfServiceInstance implements Servicium {

    protected USII                      mUSII;

    protected Service                   mService;

    public WolfServiceInstance( long clientId, Service service ){
        this.mUSII            = BindUSII.wrap( clientId, service.getId() );
        this.mService         = service;
    }

    @Override
    public Identification getId() {
        return this.mUSII.getServiceId();
    }

    @Override
    public USII getUSII() {
        return this.mUSII;
    }

    @Override
    public Processum getProcessObject() {
        return null;
    }
}
