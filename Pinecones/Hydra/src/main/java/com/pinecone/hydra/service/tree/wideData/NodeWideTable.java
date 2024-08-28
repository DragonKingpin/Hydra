package com.pinecone.hydra.service.tree.wideData;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface NodeWideTable extends Pinenut {
    NodeWideData get(GUID guid);

    void put(GUID guid, NodeWideData nodeWideData);

    void remove(GUID guid);
}
