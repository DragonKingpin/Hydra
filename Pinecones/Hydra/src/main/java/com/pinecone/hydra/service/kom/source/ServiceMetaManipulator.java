package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.meta.GenericServiceNodeMeta;

public interface ServiceMetaManipulator {
    void insert( ServiceElement serviceElement );
    void remove(GUID guid);
    void update( ServiceElement serviceElement );
    ServiceElement getServiceMeta(GUID guid);
}
