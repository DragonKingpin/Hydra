package com.pinecone.hydra.deploy.kom.source;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.dao.GUIDNameManipulator;
import com.pinecone.hydra.deploy.kom.entity.Namespace;

public interface TaskNamespaceManipulator extends GUIDNameManipulator {
    void insert(Namespace ns);

    void remove(GUID guid);

    Namespace getNamespace(GUID guid);

    void update(Namespace ns);

    List<Namespace > fetchNamespaceNodeByName(String name);
}
