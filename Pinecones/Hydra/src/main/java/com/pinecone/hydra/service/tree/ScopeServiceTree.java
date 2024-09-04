package com.pinecone.hydra.service.tree;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.entity.MetaNodeWideEntity;
import com.pinecone.hydra.service.tree.meta.NodeMetadata;
import com.pinecone.hydra.service.tree.nodes.ServiceTreeNode;

public interface ScopeServiceTree extends Pinenut {
    String DefaultMetaNodeClassification = "ClassificationNode";
    String DefaultMetaNodeApplication    = "ApplicationNode";
    String DefaultMetaNodeService        = "ServiceNode";

    default boolean isDefauteMetaNode( ServiceTreeNode node ) {
        return  !node.getMetaType().equals( ScopeServiceTree.DefaultMetaNodeClassification ) ||
                !node.getMetaType().equals( ScopeServiceTree.DefaultMetaNodeApplication    ) ||
                !node.getMetaType().equals( ScopeServiceTree.DefaultMetaNodeService        ) ;
    }

    GUID addNode( ServiceTreeNode node );

    void removeNode( GUID guid );

    ServiceTreeNode getNode( GUID guid );

    ServiceTreeNode parsePath( String path ) ;

    void remove(GUID guid);

    MetaNodeWideEntity getWideMeta(GUID guid);
}
