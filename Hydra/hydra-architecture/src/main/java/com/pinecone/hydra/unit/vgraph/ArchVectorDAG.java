package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.util.id.GUID;

public abstract class ArchVectorDAG implements VectorDAG {

    protected GUID                                      mGraphGuid;
    protected VectorGraphConfig                         mVectorGraphConfig;

    // Temporary Graph
    public ArchVectorDAG( GUID graphGuid, VectorGraphConfig vectorGraphConfig ) {
        this.mVectorGraphConfig = vectorGraphConfig;
        this.mGraphGuid = graphGuid;
    }

    @Override
    public VectorGraphConfig getConfig() {
        return this.mVectorGraphConfig;
    }

    @Override
    public GUID getGraphGuid() {
        return this.mGraphGuid;
    }

}
