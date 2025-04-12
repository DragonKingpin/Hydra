package com.pinecone.hydra.unit.vgraph;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.unit.vgraph.entity.GraphNode;

public class GraphNodePair implements Pinenut {
    private GraphNode mGraphNode;

    private String sCurrentPath;

    public GraphNodePair( GraphNode graphNode, String currentPath){
        this.mGraphNode = graphNode;
        this.sCurrentPath = currentPath;
    }

    public GraphNodePair(){}

    public GraphNode getGraphNode() {
        return mGraphNode;
    }

    public void setGraphNode(GraphNode graphNode) {
        this.mGraphNode = graphNode;
    }

    public String getCurrentPath() {
        return sCurrentPath;
    }

    public void setCurrentPath(String sCurrentPath) {
        this.sCurrentPath = sCurrentPath;
    }
}
