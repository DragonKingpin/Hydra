package com.acorn.redqueen.service.registry.husky.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.umc.wolf.client.UlfClient;

public class HuskyServiceClientTransportFactory implements Pinenut {

    public HuskyServiceClientTransport create(
            UlfClient rpcClient,
            GuidAllocator guidAllocator,
            HuskyServiceClientTransportConfig config
    ) {
        return new HuskyServiceClientTransport( rpcClient, guidAllocator, config );
    }

}
