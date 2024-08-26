package com.pinecone.hydra.service.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.tree.meta.NodeMetadata;

public interface ScopeServiceTree extends Pinenut {
    String DefaultMetaNodeClassification = "com.pinecone.hydra.service.tree.nodes.GenericClassificationNode";
    String DefaultMetaNodeApplication    = "com.pinecone.hydra.service.tree.nodes.GenericApplicationNode";
    String DefaultMetaNodeService        = "com.pinecone.hydra.service.tree.nodes.GenericServiceNode";

//    default boolean isDefauteMetaNode( NodeMetadata nodeMetadata ) {
//        return  !nodeMetadata.getMetaType().equals( ScopeServiceTree.DefaultMetaNodeClassification ) ||
//                !nodeMetadata.getMetaType().equals( ScopeServiceTree.DefaultMetaNodeApplication    ) ||
//                !nodeMetadata.getMetaType().equals( ScopeServiceTree.DefaultMetaNodeService        ) ;
//    }
}
