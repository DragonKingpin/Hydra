package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.meta.GenericApplicationNodeMeta;

public interface ApplicationMetaManipulator extends Pinenut {
    //ApplicationDescription的CRUD
    void insert(GenericApplicationNodeMeta applicationMeta);

    void remove(GUID guid);

    GenericApplicationNodeMeta getApplicationMeta(GUID guid);

    void update(GenericApplicationNodeMeta applicationMeta);
}
