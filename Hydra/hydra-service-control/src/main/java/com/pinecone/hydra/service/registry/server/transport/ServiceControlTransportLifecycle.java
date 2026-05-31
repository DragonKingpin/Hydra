package com.pinecone.hydra.service.registry.server.transport;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;

public interface ServiceControlTransportLifecycle extends Pinenut {

    void startService() throws ServiceControlRPCException;

    void terminateService() throws IllegalStateException;

    boolean isStarted();

    boolean isTerminated();

}
