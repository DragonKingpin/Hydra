package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ServiceClientStateSynchronizedHandler extends Pinenut {

    void afterServiceClientStateSynchronized( String reason );

}
