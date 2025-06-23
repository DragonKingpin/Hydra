package com.walnut.odin.proc.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umct.stereotype.Iface;
import com.walnut.odin.proc.dto.RemoteTerminationReport;
import com.walnut.odin.proc.dto.UProcessHandlerDTO;

@Iface
public interface SlaveProcessLifecycleIface extends Pinenut {

    long reportClientInitialized( long clientId );

    void registerRemoteProcess( long clientId, UProcessHandlerDTO processDTO );

    void notifyProcessTerminated( long clientId, RemoteTerminationReport terminationReport );

}
