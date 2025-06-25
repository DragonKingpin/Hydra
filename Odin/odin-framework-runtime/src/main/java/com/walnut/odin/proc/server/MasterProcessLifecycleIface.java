package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessMirrorDTO;
import com.walnut.odin.proc.dto.UProcessRuntimeMeta;

@Iface
public interface MasterProcessLifecycleIface extends Pinenut {

    void startRemoteUProcess( String processId );

    RemoteVitalizationResponse vitalizeRemoteUProcess( UProcessMirrorDTO handlerDTO );

    RemoteVitalizationResponse createRemoteUProcess( UProcessMirrorDTO handlerDTO );

    boolean hasOwnProcess( String processId );

    boolean containProcess( String processId );

    UProcessRuntimeMeta queryRemoteProcessRuntimeMeta( String processId );

}
