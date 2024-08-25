package com.pinecone.hydra.service.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface MetaNodeOperator extends Pinenut {
    GUID insert(NodeWideData nodeWideData);

    void remove(GUID guid);

    NodeWideData get(GUID guid);

    void update(NodeWideData nodeWideData);
}
