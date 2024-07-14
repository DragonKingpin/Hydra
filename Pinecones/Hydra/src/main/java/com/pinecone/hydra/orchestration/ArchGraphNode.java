package com.pinecone.hydra.orchestration;

public abstract class ArchGraphNode implements GraphNode {
    protected int             mnStratumId;
    protected GraphNode       mParent;

    protected void setParent( GraphNode parent ) {
        this.mParent = parent;
    }

    @Override
    public int getStratumId() {
        return this.mnStratumId;
    }

    @Override
    public GraphNode parent() {
        return this.mParent;
    }
}
