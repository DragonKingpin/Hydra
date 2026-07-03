package com.walnut.odin.proc.control;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;

import java.util.List;

@Iface
public interface RemoteProcessControlFrameIface extends Pinenut {

    RemoteProcessControlFrame exchangeControlFrame( RemoteProcessControlFrame frame );

    RemoteProcessControlFrame musterClient( long nClientId, String szFrameGuid, List<UProcessMirrorDTO> processMirrors );

    RemoteProcessControlFrame reportProcessMirror( long nClientId, String szSessionGuid, String szFrameGuid, UProcessMirrorDTO processMirror );

    RemoteProcessControlFrame detachClient( long nClientId, String szSessionGuid, String szFrameGuid );
}
