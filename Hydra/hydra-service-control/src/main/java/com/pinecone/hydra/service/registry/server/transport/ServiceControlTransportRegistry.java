package com.pinecone.hydra.service.registry.server.transport;

import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.server.transport.entity.ServiceTransportHandle;

public interface ServiceControlTransportRegistry extends Pinenut {

    ServiceControlTransportRegistry hookTransport( ServiceControlTransport transport );

    ServiceControlTransport queryTransport( long nClientId );

    ServiceTransportHandle queryTransportHandle( long nClientId );

    Collection<ServiceTransportHandle> transportHandles();

    ServiceControlTransport requireTransport( long nClientId ) throws ServiceControlRPCException;

    Collection<ServiceControlTransport> transports();

    void bindClient( long nClientId, ServiceControlTransport transport );

    void detachClient( long nClientId );

    boolean hasClient( long nClientId );

}
