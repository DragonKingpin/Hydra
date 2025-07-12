package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceElement;

public interface ServiceInstanceManipulator extends Pinenut {
    void initServiceInstance( ServiceInstanceElement element );

    ServiceInstanceElement queryServiceInstance( GUID serviceId );

    void updateServiceInstance( ServiceInstanceElement element );
}
