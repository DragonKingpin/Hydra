package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.umct.stereotype.Iface;

@Iface
public interface ClientLifecycleIface extends Pinenut {
    boolean destroyServiceInstance( GUID instanceGuid );
}
