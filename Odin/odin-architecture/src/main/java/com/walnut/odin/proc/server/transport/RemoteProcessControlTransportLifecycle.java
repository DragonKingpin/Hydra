package com.walnut.odin.proc.server.transport;

import com.pinecone.framework.system.prototype.Pinenut;
import com.walnut.odin.proc.RemoteProcessServiceRPCException;

public interface RemoteProcessControlTransportLifecycle extends Pinenut {

    void startService() throws RemoteProcessServiceRPCException;

    void terminateService() throws IllegalStateException;

    boolean isStarted();

    boolean isTerminated();

}
