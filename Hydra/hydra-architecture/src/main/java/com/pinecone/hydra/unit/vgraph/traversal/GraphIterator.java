package com.pinecone.hydra.unit.vgraph.traversal;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.VectorDAG;

public interface GraphIterator extends Pinenut {
    boolean containNode( VectorDAG vectorDAG, GUID targetNodeGuid );

}
