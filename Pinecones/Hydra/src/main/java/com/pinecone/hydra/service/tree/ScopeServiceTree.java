package com.pinecone.hydra.service.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.tree.meta.NodeMetadata;

public interface ScopeServiceTree extends Pinenut {
    String DefaultMetaNodeClassification = "Classification";
    String DefaultMetaNodeApplication    = "Application";
    String DefaultMetaNodeService        = "Service";

    default boolean isDefauteMetaNode( NodeMetadata nodeMetadata ) {
        return  !nodeMetadata.getMetaType().equals( ScopeServiceTree.DefaultMetaNodeClassification ) ||
                !nodeMetadata.getMetaType().equals( ScopeServiceTree.DefaultMetaNodeApplication    ) ||
                !nodeMetadata.getMetaType().equals( ScopeServiceTree.DefaultMetaNodeService        ) ;
    }
}
