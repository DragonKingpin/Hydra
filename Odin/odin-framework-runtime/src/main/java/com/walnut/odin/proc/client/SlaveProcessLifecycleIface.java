package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.odin.proc.dto.RemoteTerminationReport;
import com.walnut.odin.proc.dto.RemoteVitalizationResponse;
import com.walnut.odin.proc.dto.UProcessMirrorDTO;

@Iface
public interface SlaveProcessLifecycleIface extends Pinenut {

    long reportClientInitialized( long clientId );

    void registerRemoteProcess( long clientId, UProcessMirrorDTO processDTO );

    void reportProcessTerminated( long clientId, RemoteTerminationReport terminationReport );

    String reportProcessCreated( long clientId, RemoteVitalizationResponse vitalizationResponse );

}
