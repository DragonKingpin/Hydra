package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.GenericNodeCommonData;

public interface CommonDataManipulator extends Pinenut {
    //NodeMetadata节点的CRUD
    void insert(GenericNodeCommonData nodeMetadata);

    void remove(GUID guid);

    GenericNodeCommonData getNodeMetadata(GUID guid);

    void update(GenericNodeCommonData nodeMetadata);
}
