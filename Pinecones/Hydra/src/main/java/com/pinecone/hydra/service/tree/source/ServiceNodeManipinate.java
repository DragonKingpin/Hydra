package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericServiceNode;

import java.util.List;

public interface ServiceNodeManipinate extends Pinenut {
    //ServiceNode的CRUD
    void saveServiceNode(GenericServiceNode serviceNode);
    void deleteServiceNode(GUID UUID);
    GenericServiceNode selectServiceNode(GUID UUID);
    void updateServiceNode(GenericServiceNode serviceNode);
    List<GenericServiceNode> selectServiceNodeByName(String name);
}
