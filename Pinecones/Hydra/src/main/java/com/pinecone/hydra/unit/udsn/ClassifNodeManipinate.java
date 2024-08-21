package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericClassificationNode;

import java.util.List;

public interface ClassifNodeManipinate extends Pinenut {
    //ClassifcationNode的CRUD
    void saveClassifNode(GenericClassificationNode classificationNode);
    void deleteClassifNode(GUID UUID);
    GenericClassificationNode selectClassifNode(GUID UUID);
    void updateClassifNode(GenericClassificationNode classificationNode);
    List<GenericClassificationNode> selectClassifNodeByName(String name);
}
