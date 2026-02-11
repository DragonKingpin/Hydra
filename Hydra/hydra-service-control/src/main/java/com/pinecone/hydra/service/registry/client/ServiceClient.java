package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.appoints.AppointNodus;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;

public interface ServiceClient extends Pinenut {
    void startService () throws ServiceControlRPCException;

    void terminateService ();

    AppointNodus getAppointNodus ();

    GuidAllocator getGuidAllocator ();

    GUID registerService( GUID serviceId, GUID deployGuid ) throws ClientServiceRegisterException;

    void deregister();

}
