package com.pinecone.hydra.service.registry.event;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface InstanceLifecycleEventHandler extends Pinenut {

    void fired( long clientId, GUID insId, GUID serviceId, InstanceLifecycleEvent event, Object caused );

}
