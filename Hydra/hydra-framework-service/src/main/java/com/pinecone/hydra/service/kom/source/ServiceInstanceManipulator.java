package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;

public interface ServiceInstanceManipulator extends Pinenut {
    void initServiceInstance( ServiceInstanceEntry element );

    ServiceInstanceEntry queryServiceInstance( GUID instanceId );

    void updateServiceInstance( ServiceInstanceEntry element );
}
