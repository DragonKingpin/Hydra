package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceInstanceCreationException;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;

public interface ServiceManagerClient extends Pinenut {
    void startService () throws ServiceControlRPCException;

    void terminateService ();

    GUID registerService( GUID serviceId, GUID deployGuid ) throws ServiceInstanceCreationException, ClientServiceRegisterException;
}
