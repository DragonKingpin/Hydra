package com.walnut.odin.proc.server.transport.grpc;

import com.walnut.odin.proc.server.transport.GenericRemoteProcessControlEventHooker;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransport;
import com.walnut.odin.proc.server.transport.RemoteProcessControlTransportRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcRemoteProcessControlEventHooker extends GenericRemoteProcessControlEventHooker {

    protected Logger log = LoggerFactory.getLogger( this.getClass() );

    public GrpcRemoteProcessControlEventHooker( RemoteProcessControlTransportRegistry transportRegistry ) {
        super( transportRegistry );
    }

    @Override
    public void onClientInitialized( RemoteProcessControlTransport transport, long clientId ) {
        super.onClientInitialized( transport, clientId );
        this.log.info(
                "[GrpcControlRegistry] [BindClient] (ClientId: `{}`, Transport: `{}`) <Done>",
                clientId,
                transport.transportType()
        );
    }

    @Override
    public void onClientDetached( RemoteProcessControlTransport transport, long clientId ) {
        super.onClientDetached( transport, clientId );
        this.log.info(
                "[GrpcControlRegistry] [DetachClient] (ClientId: `{}`, Transport: `{}`) <Done>",
                clientId,
                transport.transportType()
        );
    }

}
