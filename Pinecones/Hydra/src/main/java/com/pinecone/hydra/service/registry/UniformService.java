package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.Identification;
import com.pinecone.framework.util.name.Namespace;
import com.pinecone.hydra.service.ArchService;
import com.pinecone.hydra.service.Serviciom;
import com.pinecone.hydra.service.kom.entity.ServiceElement;

public class UniformService extends ArchService implements Serviciom {

    public UniformService( Identification serviceId, ServiceElement serviceElement ) {
        super( serviceId, serviceElement );
    }

    @Override
    public Namespace getGroupNamespace() {
        return null;
    }

    @Override
    public String getGroupName() {
        return null;
    }

    @Override
    public Object getProcessImageObject() {
        return null;
    }

}
