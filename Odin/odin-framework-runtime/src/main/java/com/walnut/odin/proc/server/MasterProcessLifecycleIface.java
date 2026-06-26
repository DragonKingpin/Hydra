package com.walnut.odin.proc.server;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.odin.proc.entity.RemoteProcessSignalResult;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;
import com.walnut.odin.proc.entity.UProcessRuntimeMeta;

@Iface
public interface MasterProcessLifecycleIface extends Pinenut {

    void startRemoteUProcess( String processId );

    RemoteProcessSignalResult signalRemoteUProcess( String processId, String signal, long graceTimeoutMillis, String reason );

    RemoteVitalizationResponse vitalizeRemoteUProcess( UProcessMirrorDTO handlerDTO );

    RemoteVitalizationResponse createRemoteUProcess( UProcessMirrorDTO handlerDTO );

    boolean hasOwnProcess( String processId );

    boolean containProcess( String processId );

    UProcessRuntimeMeta queryRemoteProcessRuntimeMeta( String processId );

}
