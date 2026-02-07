package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.uma.DuplexAppointClient;

public interface ServiceClient extends Pinenut {
    void startService () throws ServiceControlRPCException;

    void terminateService ();

    DuplexAppointClient getDuplexAppointClient ();

    GuidAllocator getGuidAllocator ();

    GUID registerService( GUID serviceId, GUID deployGuid ) throws ClientServiceRegisterException;

    void deregister();

}
