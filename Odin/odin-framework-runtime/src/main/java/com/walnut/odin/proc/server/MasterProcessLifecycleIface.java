package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessMirrorDTO;

@Iface
public interface MasterProcessLifecycleIface extends Pinenut {

    void startRemoteUProcess( String processId );

    RemoteVitalizationResponse vitalizeRemoteUProcess( String imageAddress, boolean isURI, UProcessMirrorDTO handlerDTO );

    RemoteVitalizationResponse createRemoteUProcess( String imageAddress, boolean isURI, UProcessMirrorDTO handlerDTO );

}
