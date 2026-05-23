package com.walnut.odin.proc.server.transport.husky;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.AddressMapping;
import com.pinecone.hydra.umct.stereotype.Controller;
import com.walnut.odin.proc.control.RemoteProcessControlFrame;
import com.walnut.odin.proc.control.RemoteProcessControlFrameIface;
import com.walnut.odin.proc.server.RemoteProcessManagerServer;
import com.walnut.odin.proc.server.transport.GenericRemoteProcessControlProtocolCoordinator;
import com.walnut.odin.proc.server.transport.RemoteProcessControlProtocolCoordinator;

@Controller
@AddressMapping( "com.walnut.odin.proc.control.RemoteProcessControlFrameIface." )
public class ReactiveRemoteProcessControlFrameController implements RemoteProcessControlFrameIface, Pinenut {

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
    public String musterClient( long nClientId, String szFrameGuid, String szSnapshotJson ) {
        return this.mProtocolCoordinator.musterClient( this.mTransport, nClientId, szFrameGuid, szSnapshotJson );
    }

    @Override
    @AddressMapping( "reportProcessMirror" )
    public String reportProcessMirror( long nClientId, String szSessionGuid, String szFrameGuid, String szProcessMirrorJson ) {
        return this.mProtocolCoordinator.reportProcessMirror( this.mTransport, nClientId, szSessionGuid, szFrameGuid, szProcessMirrorJson );
    }

    @Override
    @AddressMapping( "detachClient" )
    public String detachClient( long nClientId, String szSessionGuid, String szFrameGuid ) {
        return this.mProtocolCoordinator.detachClient( this.mTransport, nClientId, szSessionGuid, szFrameGuid );
    }
}
