package com.pinecone.hydra.service.registry;

import com.pinecone.hydra.service.ServiceControllerIfce;
import com.pinecone.hydra.service.kom.entity.ServiceElement;

import java.util.Map;

public class GenericServiceControlBlock implements ServiceControlBlock {
    protected ServiceControllerIfce     mServiceControllerIfce;

    protected long                      mClientId;

    protected ServiceElement            mServiceMetaData;


    protected Map<String, Object >      mExtraMetaData;


    public GenericServiceControlBlock( long clientId, ServiceElement serviceElement ){
        this.mClientId = clientId;
        this.mServiceMetaData = serviceElement;
    }
}
