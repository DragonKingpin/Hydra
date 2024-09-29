package com.pinecone.hydra.registry.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface RegistryNodeManipulator extends GUIDNameManipulator {
    void insert( ConfigNode configNode );

    void remove( GUID guid );

    ConfigNode getConfigurationNode( GUID guid );

    void update( ConfigNode configNode );

    List<GUID > getGuidsByName( String name );

    List<GUID> getALL();

    void updateName(GUID guid ,String name);
}
