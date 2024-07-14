package com.pinecone.hydra.orchestration;

import java.util.List;

public interface GraphStratum extends GraphNode {
    List<GraphNode > getChildren();
}
