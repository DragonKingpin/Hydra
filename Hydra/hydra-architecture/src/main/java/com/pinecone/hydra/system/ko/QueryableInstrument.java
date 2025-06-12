package com.pinecone.hydra.system.ko;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.unit.imperium.entity.EntityNode;

public interface QueryableInstrument extends KernelObjectInstrument {

    String getPath( GUID objectGuid );

    String querySystemKernelObjectPath( GUID objectGuid ) ;

    GUID queryGUIDByPath( String path );

    EntityNode queryNode( String path );

}
