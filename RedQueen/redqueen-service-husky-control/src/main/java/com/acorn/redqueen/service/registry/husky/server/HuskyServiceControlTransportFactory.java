package com.acorn.redqueen.service.registry.husky.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransport;
import com.pinecone.hydra.umc.wolf.server.UlfServer;

public class HuskyServiceControlTransportFactory implements Pinenut {

    public static ServiceControlTransport create( ServiceManager serviceManager, UlfServer rpcServer ) {
        return new HuskyServiceControlTransport( serviceManager, rpcServer );
    }

}

