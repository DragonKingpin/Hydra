package com.walnut.odin.conduct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.proc.ProcessManager;
import com.walnut.odin.conduct.entity.RegimentJoinResponse;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;

public interface CollectiveTaskLegionary extends Pinenut {

    String getName();

    long getClientId();

    ProcessManager processManager();

    void startService () throws RemoteProcessServiceRPCException;

    RegimentJoinResponse joinRegiment () throws RegimentException;

}
