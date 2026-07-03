package com.pinecone.hydra.service.registry.client.transport;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.client.ServiceClientStateSynchronizedHandler;
import com.pinecone.hydra.service.registry.client.control.ServiceClientManipulationHandler;
import com.pinecone.hydra.service.registry.client.port.ServicePort;

public interface ServiceClientTransport extends Pinenut {

    long getClientId();

    ServiceClientTransportType transportType();

    boolean isReady();

    void connect() throws ServiceClientTransportException;

    <T extends ServicePort> T getPort( Class<T> portClass ) throws ServiceClientTransportException;

    default void registerStateSynchronizedHandler( ServiceClientStateSynchronizedHandler handler ) {
    }

    default void deregisterStateSynchronizedHandler( ServiceClientStateSynchronizedHandler handler ) {
    }

    default void registerManipulationHandler( ServiceClientManipulationHandler handler ) {
    }

    default void deregisterManipulationHandler( ServiceClientManipulationHandler handler ) {
    }

    void disconnect();

}
