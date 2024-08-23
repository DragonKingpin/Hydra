package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericClassificationNode;

import java.util.List;

public interface ClassifNodeManipulator extends Pinenut {
    //ClassifcationNode的CRUD
    void saveClassifNode(GenericClassificationNode classificationNode);

    void deleteClassifNode(GUID guid);

    GenericClassificationNode selectClassifNode(GUID guid);

    void updateClassifNode(GenericClassificationNode classificationNode);

    List<GenericClassificationNode> selectClassifNodeByName(String name);
}
