package com.pinecone.hydra.task.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.task.kom.entity.Namespace;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;

import java.util.List;

public interface TaskNamespaceManipulator extends GUIDNameManipulator {
    void insert( Namespace ns );

    void remove( GUID guid );

    Namespace getNamespace( GUID guid );

    void update( Namespace ns );

    List<Namespace > fetchNamespaceNodeByName(String name );
}
