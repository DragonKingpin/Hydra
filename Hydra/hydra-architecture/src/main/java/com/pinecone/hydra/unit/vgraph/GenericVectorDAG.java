package com.pinecone.hydra.unit.vgraph;

import com.pinecone.hydra.unit.vgraph.source.VectorGraphMasterManipulator;

public class GenericVectorDAG extends ArchVectorDAG implements VectorDAG {
    public GenericVectorDAG(VectorGraphMasterManipulator masterManipulator, VectorGraphConfig vectorGraphConfig) {
        super(masterManipulator, vectorGraphConfig);
    }
}
