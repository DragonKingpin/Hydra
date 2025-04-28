package com.pinecone.hydra.unit.vgraph.layer.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.unit.vgraph.layer.LayerNamespace;

public interface NamespaceManipulator extends GUIDNameManipulator {
    void insert( LayerNamespace layerNamespace );

    void remove( GUID guid );

    LayerNamespace query( GUID guid );
}
