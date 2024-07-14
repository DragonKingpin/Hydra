package com.pinecone.hydra.orchestration;

import java.util.ArrayList;
import java.util.List;

public abstract class ArchStratum implements GraphStratum {
    protected List<GraphNode >   mChildren;
    protected ArchGraphNode      mParent;

    ArchStratum() {
        this.mChildren = new ArrayList<>();
    }

    @Override
    public ArchGraphNode parent() {
        return this.mParent;
    }

    @Override
    public List<GraphNode > getChildren() {
        return this.mChildren;
    }
}
