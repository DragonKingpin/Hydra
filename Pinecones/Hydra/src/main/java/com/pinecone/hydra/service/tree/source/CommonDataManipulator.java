package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.GenericNodeCommonData;

public interface CommonDataManipulator extends Pinenut {
    //NodeMetadata节点的CRUD
    void insert(GenericNodeCommonData nodeMetadata);

    void delete(GUID guid);

    GenericNodeCommonData getNodeMetadata(GUID guid);

    void update(GenericNodeCommonData nodeMetadata);
}
