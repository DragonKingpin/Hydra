package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;

import java.util.List;

public interface ClassifNodeManipulator extends Pinenut {
    //ClassifcationNode的CRUD
    void insert(GenericClassificationNode classificationNode);

    void delete(GUID guid);

    GenericClassificationNode getClassifNode(GUID guid);

    void update(GenericClassificationNode classificationNode);

    List<GenericClassificationNode> fetchClassifNodeByName(String name);
}
