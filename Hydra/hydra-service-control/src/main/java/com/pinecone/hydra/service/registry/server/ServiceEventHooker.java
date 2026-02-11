package com.pinecone.hydra.service.registry.server;

import java.util.function.Supplier;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.appoint.RegisteredServiceClient;

public interface ServiceEventHooker extends Pinenut {

    void afterNewConnectionInbound(
            Long clientId, Object connectId, Object connection, Object context,
            Supplier<RegisteredServiceClient> constructor
    );

    void afterConnectionDetach( Long clientId, Object channelId, Object connection );

}
