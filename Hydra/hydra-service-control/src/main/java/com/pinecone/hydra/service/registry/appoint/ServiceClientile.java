package com.pinecone.hydra.service.registry.appoint;

import java.net.SocketAddress;
import java.util.Collection;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ServiceClientile extends Pinenut {

    /**
     * One Client ID corresponds to one instance and can only have one address.
     * For scenarios where a single client multiple connections is opened, there can only be one main address.
     * 一个 ClientId，对应一个实例，只能有一个地址
     * 对于开了单客户端多复用连接的场景，只能有一个主地址
     */
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
