package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.nodes.GenericNamespace;

import java.util.List;

public interface ServiceNamespaceManipulator extends Pinenut {
    void insert(GenericNamespace classificationNode);

    void remove(GUID guid);

    GenericNamespace getNamespace(GUID guid);

    void update(GenericNamespace classificationNode);

    List<GenericNamespace> fetchNamespaceNodeByName(String name);
}
