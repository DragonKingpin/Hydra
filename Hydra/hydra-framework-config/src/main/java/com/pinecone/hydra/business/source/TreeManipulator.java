package com.pinecone.hydra.business.source;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.business.entity.GenericNodeTree;
import com.pinecone.hydra.business.entity.NodeTree;

public interface TreeManipulator extends Pinenut {

    void insert( NodeTree nodeTree );

    void insertOwned( GUID guid, GUID parentGuid );

    void remove( GUID guid );

    GenericNodeTree get( GUID guid );

    GUID fetchParentGuid( GUID guid );

    List<GUID > fetchChildrenGuids( GUID parentGuid );

    List<GUID > fetchRootGuids();

    void updateParentGuid( GUID targetGuid, GUID parentGuid );

    void removeTreeNodeByParentGuid( GUID parentGuid );

    long countNode( GUID guid, GUID parentGuid );
}
