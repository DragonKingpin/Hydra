package com.pinecone.hydra.unit.iqueue;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface QueueExistManipulator extends Pinenut {
    void insertQueueExist(GUID layerGuid );

    void insertQueueNotExist(GUID layerGuid );

    void setQueueExist(GUID layerGuid);

    void setQueueNotExist(GUID layerGuid);

    boolean isExist( GUID layer_guid );
}
