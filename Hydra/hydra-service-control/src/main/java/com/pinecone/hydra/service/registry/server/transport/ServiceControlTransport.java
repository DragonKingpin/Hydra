package com.pinecone.hydra.service.registry.server.transport;

import java.util.Collection;
import java.util.Collections;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.server.transport.entity.ServiceTransportConnection;

public interface ServiceControlTransport extends ServiceControlTransportLifecycle {

    ServiceControlTransportType transportType();

    boolean containsClient( long nClientId );

    void registerController( Object controller ) throws ServiceControlRPCException;

    default boolean supportsRuntimeIfaceCompile() {
        return false;
    }

    void compileIface( Class<?> ifaceClass, boolean bAsIface ) throws ServiceControlRPCException;

    default Collection<ServiceTransportConnection> queryClientConnections( long nClientId ) {
        return Collections.emptyList();
    }

    default void shutdownClientService( long nClientId, GUID instanceGuid, String szReason ) throws ServiceControlRPCException {
        throw new ServiceControlRPCException( "Service shutdown manipulation is not supported by transport `" + this.transportType() + "`." );
    }

}
