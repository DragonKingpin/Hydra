package com.pinecone.hydra.atlas.graph.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

public interface TaskGraphNode extends GraphNode {
    void setName( String name );

    GUID getTaskGuid();

    void setTaskGuid( GUID taskGuid );
}
