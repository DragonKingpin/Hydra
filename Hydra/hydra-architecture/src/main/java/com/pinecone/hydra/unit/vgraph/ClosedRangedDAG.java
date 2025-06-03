package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface ClosedRangedDAG extends VectorDAG {

    List<GUID> getSinkGuids();

}
