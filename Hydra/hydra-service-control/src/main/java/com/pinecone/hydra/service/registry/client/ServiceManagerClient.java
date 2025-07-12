package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.registry.exception.ClientRegisterServiceException;
import com.pinecone.hydra.service.registry.exception.CreateServiceInstanceException;
import com.pinecone.hydra.service.registry.exception.RpcInitException;

public interface ServiceManagerClient extends Pinenut {
    void startService () throws RpcInitException;

    void terminateService ();

    GUID registerService( GUID serviceId, GUID deployGuid ) throws CreateServiceInstanceException, ClientRegisterServiceException;
}
