package com.pinecone.hydra.service.registry.server;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

import com.pinecone.hydra.service.registry.appoint.RegisteredServiceClient;

public class UniformServiceEventHooker implements ServiceEventHooker {

    protected UniformServiceManager mUniformServiceManager;

    public UniformServiceEventHooker( UniformServiceManager manager ) {
        this.mUniformServiceManager = manager;
    }


    @Override
    public void afterNewConnectionInbound(
            Long clientId, Object connectId, Object connection, Object context,
            Supplier<RegisteredServiceClient> constructor
    ) {
        this.mUniformServiceManager.mClientRegistry.compute( clientId, (key, ins ) -> {
            if ( ins == null ) {
                ins = constructor.get();
            }
            ins.afterNewConnectionInbound( clientId, connectId, connection, context );
            return ins;
        } );
    }

    @Override
    public void afterConnectionDetach( Long clientId, Object channelId, Object connection ) {
        synchronized ( this.mUniformServiceManager.mClientRegistry ) {
            RegisteredServiceClient client = this.mUniformServiceManager.mClientRegistry.get( clientId );
            // It’s not thread-safe beyond this critical zone, as the size may be mutated by other threads after this point.
            // 该临界区后面线程并不安全, size 可能在该临界区后被其他线程破坏.
            if ( client != null ) {
                client.afterConnectionDetach( clientId, channelId, connection );

                if ( client.connectionCount() < 1 ) {
                    this.mUniformServiceManager.mClientRegistry.remove( clientId );
                    this.mUniformServiceManager.deregisterServiceInstance( clientId );
                }
            }
        }
    }

}
