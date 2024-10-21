package com.pinecone.hydra.service.kom;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.MetaNodeWideEntity;
import com.pinecone.hydra.service.kom.nodes.ServiceTreeNode;

public interface ServicesInstrument extends Pinenut {
    String DefaultMetaNodeClassification = "ClassificationNode";
    String DefaultMetaNodeApplication    = "ApplicationNode";
    String DefaultMetaNodeService        = "ServiceNode";

    default boolean isDefauteMetaNode( ServiceTreeNode node ) {
        return  !node.getMetaType().equals( ServicesInstrument.DefaultMetaNodeClassification ) ||
                !node.getMetaType().equals( ServicesInstrument.DefaultMetaNodeApplication    ) ||
                !node.getMetaType().equals( ServicesInstrument.DefaultMetaNodeService        ) ;
    }

    GUID addNode( ServiceTreeNode node );

    void removeNode( GUID guid );

    ServiceTreeNode getNode( GUID guid );

    ServiceTreeNode parsePath( String path ) ;

    void remove( GUID guid );

    MetaNodeWideEntity getWideMeta( GUID guid );

    String getPath( GUID guid );
}
