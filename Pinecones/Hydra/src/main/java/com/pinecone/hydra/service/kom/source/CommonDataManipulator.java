package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.NodeCommonData;

public interface CommonDataManipulator extends Pinenut {
    //NodeMetadata节点的CRUD
    void insert(NodeCommonData node);

    void remove(GUID guid);

    NodeCommonData getNodeCommonData(GUID guid);

    void update(NodeCommonData node);
}
