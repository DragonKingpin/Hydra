package com.walnut.odin.proc.control;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface RemoteProcessControlFrameIface extends Pinenut {

    RemoteProcessControlFrame exchangeControlFrame( RemoteProcessControlFrame frame );

    String musterClient( long nClientId, String szFrameGuid, String szSnapshotJson );

    String reportProcessMirror( long nClientId, String szSessionGuid, String szFrameGuid, String szProcessMirrorJson );

    String detachClient( long nClientId, String szSessionGuid, String szFrameGuid );
}
