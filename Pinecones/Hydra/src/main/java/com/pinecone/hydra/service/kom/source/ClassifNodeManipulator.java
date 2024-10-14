package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.nodes.GenericNamespace;

import java.util.List;

public interface ClassifNodeManipulator extends Pinenut {
    //ClassifcationNode的CRUD
    void insert(GenericNamespace classificationNode);

    void remove(GUID guid);

    GenericNamespace getClassifNode(GUID guid);

    void update(GenericNamespace classificationNode);

    List<GenericNamespace> fetchClassifNodeByName(String name);
}
