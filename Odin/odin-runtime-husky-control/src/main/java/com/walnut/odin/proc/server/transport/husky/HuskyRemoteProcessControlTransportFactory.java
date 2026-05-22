package com.walnut.odin.proc.server.transport.husky;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;

public class HuskyRemoteProcessControlTransportFactory implements Pinenut {

    public static RemoteProcessControlTransport create( RemoteProcessManagerServer remoteProcessManagerServer, UlfServer rpcServer ) {
        return new HuskyRemoteProcessControlTransport( remoteProcessManagerServer, rpcServer );
    }

}
