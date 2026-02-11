package com.pinecone.hydra.service.registry.appoint;

import java.net.SocketAddress;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;

public interface RegisteredServiceClient extends Pinenut {

    SocketAddress getRemoteAddress();

    void afterNewConnectionInbound( Long clientId, Object connectId, Object connection, Object context );

    void afterConnectionDetach( Long clientId, Object channelId, Object connection );

    ServiceAppointServer serviceAppointServer();

    long getClientId();

    int connectionCount();

    boolean isDefunct();


    /**
     * Some servers may not be able to obtain connection-id.
     */
    Object queryNativeConnection( Object connectionIdentity );

    Collection<?> connections();

    void shutdown();



}
