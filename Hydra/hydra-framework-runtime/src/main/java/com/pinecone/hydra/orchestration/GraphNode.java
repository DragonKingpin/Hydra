package com.pinecone.hydra.orchestration;

import com.pinecone.framework.system.prototype.Pinenut;

public interface GraphNode extends Pinenut {
    int getStratumId();

    GraphNode parent();

    default GraphNode root() {
        GraphNode p = this.parent();
        if( p == null ) {
            return this;
        }

        return p.root();
    }
}
