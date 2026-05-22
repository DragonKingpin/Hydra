package com.walnut.odin.proc.server.transport;

import com.walnut.odin.proc.server.transport.RemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportRegistry;

public class GenericRemoteProcessControlEventHooker implements RemoteProcessControlEventHooker {

    protected RemoteProcessControlTransportRegistry    mTransportRegistry;

    public GenericRemoteProcessControlEventHooker( RemoteProcessControlTransportRegistry transportRegistry ) {
        this.mTransportRegistry = transportRegistry;
    }

    @Override
    public void onClientInitialized( RemoteProcessControlTransport transport, long clientId ) {
        this.mTransportRegistry.bindClient( clientId, transport );
    }

    @Override
    public void onClientDetached( RemoteProcessControlTransport transport, long clientId ) {
        this.mTransportRegistry.detachClient( clientId );
    }

}
