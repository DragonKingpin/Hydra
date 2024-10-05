package com.pinecone.hydra.registry.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.Namespace;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface RegistryNSNodeManipulator extends GUIDNameManipulator {
    void insert( Namespace namespace);

    void remove( GUID guid );

    boolean isNamespaceNode( GUID guid );

    Namespace getNamespaceWithMeta( GUID guid );

    void update( Namespace namespace);

    List<GUID > getGuidsByName( String name );

    List<GUID > getGuidsByNameID( String name, GUID guid );

    List<GUID > dumpGuid();

    void updateName( GUID guid, String name );
}
