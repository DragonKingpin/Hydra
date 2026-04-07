package com.pinecone.hydra.system.ko.handle;

import com.pinecone.framework.util.id.GUID;

public interface AppliableKHandle extends KHandle {

    KHandle applyTreeNodeName( String szTreeNodeName );

    KHandle applyTreeNodeGuid( GUID treeNodeGuid ) ;

}
