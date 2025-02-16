package com.pinecone.hydra.service.registry;

import com.pinecone.hydra.service.kom.entity.ServiceElement;

import java.util.Map;

public class GenericServiceControlBlock implements ServiceControlBlock {
    // Iface xxx

    // Object

    protected long mClientId;

    protected ServiceElement mServiceMetaData;

    // ....

    protected Map<String, Object > mExtraMetaData;
}
