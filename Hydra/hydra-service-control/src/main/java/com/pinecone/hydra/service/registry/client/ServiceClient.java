package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.appoints.AppointNodus;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.client.control.ServiceClientManipulationHandler;
import com.pinecone.hydra.service.registry.client.port.ServiceLifecyclePort;
import com.pinecone.hydra.service.registry.client.port.ServiceMetaPort;

public interface ServiceClient extends Pinenut {
    void startService () throws ServiceControlRPCException;

    void terminateService ();

    long getClientId();

    AppointNodus getAppointNodus ();

    GuidAllocator getGuidAllocator ();

    GUID registerService( GUID serviceId, GUID deployGuid ) throws ClientServiceRegisterException;

    void deregister();

    default ServiceLifecyclePort lifecycle() {
        throw new UnsupportedOperationException( "Service lifecycle port is not supported." );
    }

    default ServiceMetaPort meta() {
        throw new UnsupportedOperationException( "Service meta port is not supported." );
    }

    default void registerStateSynchronizedHandler( ServiceClientStateSynchronizedHandler handler ) {
    }

    default void deregisterStateSynchronizedHandler( ServiceClientStateSynchronizedHandler handler ) {
    }

    default void registerManipulationHandler( ServiceClientManipulationHandler handler ) {
    }

    default void deregisterManipulationHandler( ServiceClientManipulationHandler handler ) {
    }

}
