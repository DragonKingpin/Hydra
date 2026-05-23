package com.walnut.odin.proc.server.transport;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.proc.control.RemoteProcessControlFrame;

public interface RemoteProcessControlProtocolCoordinator extends Pinenut {

    RemoteProcessControlFrame exchangeControlFrame( RemoteProcessControlTransport transport, RemoteProcessControlFrame frame );

    String musterClient( RemoteProcessControlTransport transport, long nClientId, String szFrameGuid, String szSnapshotJson );

    String reportProcessMirror( RemoteProcessControlTransport transport, long nClientId, String szSessionGuid, String szFrameGuid, String szProcessMirrorJson );

    String detachClient( RemoteProcessControlTransport transport, long nClientId, String szSessionGuid, String szFrameGuid );
}
