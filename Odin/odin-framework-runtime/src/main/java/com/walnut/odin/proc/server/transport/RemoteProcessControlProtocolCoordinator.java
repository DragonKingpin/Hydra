package com.walnut.odin.proc.server.transport;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.proc.control.RemoteProcessControlFrame;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;

import java.util.List;

public interface RemoteProcessControlProtocolCoordinator extends Pinenut {

    RemoteProcessControlFrame exchangeControlFrame( RemoteProcessControlTransport transport, RemoteProcessControlFrame frame );

    RemoteProcessControlFrame musterClient( RemoteProcessControlTransport transport, long nClientId, String szFrameGuid, List<UProcessMirrorDTO> processMirrors );

    RemoteProcessControlFrame reportProcessMirror( RemoteProcessControlTransport transport, long nClientId, String szSessionGuid, String szFrameGuid, UProcessMirrorDTO processMirror );

    RemoteProcessControlFrame detachClient( RemoteProcessControlTransport transport, long nClientId, String szSessionGuid, String szFrameGuid );
}
