package com.pinecone.hydra.registry.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;

import java.util.List;

public interface RegistryNodeManipulator extends Pinenut {
    void insert( ConfigNode configNode );

    void remove( GUID guid );

    ConfigNode getConfigurationNode( GUID guid );

    void update( ConfigNode configNode );

    List<GUID > getNodeByName( String name );
    List<GUID> getALL();
}
