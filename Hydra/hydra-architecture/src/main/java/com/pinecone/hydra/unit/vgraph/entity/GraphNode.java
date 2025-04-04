package com.pinecone.hydra.unit.vgraph.entity;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface GraphNode extends Pinenut {
    String getName();

    GUID getGuid();
}
