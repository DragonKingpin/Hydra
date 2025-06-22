package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.odin.proc.dto.UProcessHandlerDTO;


@Controller
@AddressMapping( "com.walnut.odin.proc.client.SlaveProcessLifecycleIface." )
public class ReactiveSlaveProcessLifecycleController implements Pinenut {

    protected RemoteProcessManagerServer  mRemoteProcessManagerServer;

    public ReactiveSlaveProcessLifecycleController( RemoteProcessManagerServer remoteProcessManagerServer ) {
        this.mRemoteProcessManagerServer = remoteProcessManagerServer;
    }

    @AddressMapping( "registerRemoteProcess" )
    public void registerRemoteProcess( long clientId, UProcessHandlerDTO processDTO ) {
        this.mRemoteProcessManagerServer.registerProcess( clientId, processDTO );
    }

    @AddressMapping( "reportClientInitialized" )
    public long reportClientInitialized( long clientId ) {
        this.mRemoteProcessManagerServer.getLogger().info( "[ClientInitializedRecall] (ClientId: {}) <Done>", clientId );
        return clientId;
    }

}
