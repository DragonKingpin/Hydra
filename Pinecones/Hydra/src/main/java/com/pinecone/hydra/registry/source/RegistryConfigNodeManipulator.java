package com.pinecone.hydra.registry.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface RegistryConfigNodeManipulator extends GUIDNameManipulator {
    void insert( ConfigNode configNode );

    void remove( GUID guid );

    ConfigNode getConfigNode( GUID guid );

    void update( ConfigNode configNode );

    List<GUID > getGuidsByName( String name );

    List<GUID > getGuidsByNameID( String name, GUID guid );

    List<GUID > dumpGuid();

    void updateName(GUID guid ,String name);

    GUID getDataAffinityGuid( GUID guid );

    void setDataAffinityGuid( GUID guid, GUID affinityGuid );
}
