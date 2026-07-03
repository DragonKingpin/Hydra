package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.odin.proc.entity.RemoteTerminationReport;
import com.walnut.odin.proc.entity.RemoteVitalizationResponse;
import com.walnut.odin.proc.entity.UProcessMirrorDTO;

@Iface
public interface SlaveProcessLifecycleIface extends Pinenut {

    void registerRemoteProcess( long clientId, UProcessMirrorDTO processDTO );

    void reportProcessTerminated( long clientId, RemoteTerminationReport terminationReport );

    String reportProcessCreated( long clientId, RemoteVitalizationResponse vitalizationResponse );

}
