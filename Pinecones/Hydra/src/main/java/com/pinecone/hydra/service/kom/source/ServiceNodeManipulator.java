package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.entity.GenericServiceElement;

import java.util.List;

public interface ServiceNodeManipulator extends Pinenut {
    //ServiceNode的CRUD
    void insert(GenericServiceElement serviceNode);
    void remove(GUID UUID);
    GenericServiceElement getServiceNode(GUID UUID);
    void update(GenericServiceElement serviceNode);
    List<GenericServiceElement> fetchServiceNodeByName(String name);
}
