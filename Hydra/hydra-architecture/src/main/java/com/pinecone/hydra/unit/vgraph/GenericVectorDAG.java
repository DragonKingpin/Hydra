package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;

import java.util.List;

public class GenericVectorDAG extends ArchVectorDAG implements VectorDAG {
    public GenericVectorDAG(List<GUID> handleNodeGuids,VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig) {
        super(handleNodeGuids,masterManipulator, vectorGraphConfig);
    }
}
