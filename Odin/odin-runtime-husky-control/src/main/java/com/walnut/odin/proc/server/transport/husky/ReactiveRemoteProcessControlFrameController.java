package com.walnut.odin.proc.server.transport.husky;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.odin.proc.control.RemoteProcessControlFrame;
import com.walnut.odin.proc.control.RemoteProcessControlFrameIface;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.GenericRemoteProcessControlProtocolCoordinator;
import com.walnut.odin.proc.server.transport.RemoteProcessControlProtocolCoordinator;

import java.util.List;

@Controller
@AddressMapping( "com.walnut.odin.proc.control.RemoteProcessControlFrameIface." )
public class ReactiveRemoteProcessControlFrameController implements RemoteProcessControlFrameIface {

    protected RemoteProcessManagerServer                mRemoteProcessManagerServer;

    protected HuskyRemoteProcessControlTransport        mTransport;

    protected RemoteProcessControlProtocolCoordinator   mProtocolCoordinator;

    public ReactiveRemoteProcessControlFrameController(
            RemoteProcessManagerServer remoteProcessManagerServer, HuskyRemoteProcessControlTransport transport
    ) {
        this.mRemoteProcessManagerServer = remoteProcessManagerServer;
        this.mTransport                  = transport;
        this.mProtocolCoordinator        = new GenericRemoteProcessControlProtocolCoordinator( remoteProcessManagerServer );
    }

    @Override
    @AddressMapping( "exchangeControlFrame" )
    public RemoteProcessControlFrame exchangeControlFrame( RemoteProcessControlFrame frame ) {
        return this.mProtocolCoordinator.exchangeControlFrame( this.mTransport, frame );
    }

    @Override
    @AddressMapping( "musterClient" )
    public RemoteProcessControlFrame musterClient( long nClientId, String szFrameGuid, List<UProcessMirrorDTO> processMirrors ) {
        return this.mProtocolCoordinator.musterClient( this.mTransport, nClientId, szFrameGuid, processMirrors );
    }

    @Override
    @AddressMapping( "reportProcessMirror" )
    public RemoteProcessControlFrame reportProcessMirror( long nClientId, String szSessionGuid, String szFrameGuid, UProcessMirrorDTO processMirror ) {
        return this.mProtocolCoordinator.reportProcessMirror( this.mTransport, nClientId, szSessionGuid, szFrameGuid, processMirror );
    }

    @Override
    @AddressMapping( "detachClient" )
    public RemoteProcessControlFrame detachClient( long nClientId, String szSessionGuid, String szFrameGuid ) {
        return this.mProtocolCoordinator.detachClient( this.mTransport, nClientId, szSessionGuid, szFrameGuid );
    }
}
