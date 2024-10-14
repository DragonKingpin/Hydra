package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.nodes.GenericServiceNode;

import java.util.List;

public interface ServiceNodeManipulator extends Pinenut {
    //ServiceNode的CRUD
    void insert(GenericServiceNode serviceNode);
    void remove(GUID UUID);
    GenericServiceNode getServiceNode(GUID UUID);
    void update(GenericServiceNode serviceNode);
    List<GenericServiceNode> fetchServiceNodeByName(String name);
}
