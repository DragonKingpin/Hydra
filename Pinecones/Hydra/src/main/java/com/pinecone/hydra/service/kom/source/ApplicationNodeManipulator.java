package com.pinecone.hydra.service.kom.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.kom.nodes.GenericApplicationNode;

import java.util.List;

public interface ApplicationNodeManipulator {
    //ApplicationNode的CRUD
    void insert(GenericApplicationNode applicationNode);
    void remove(GUID guid);
    GenericApplicationNode getApplicationNode(GUID guid);
    void update(GenericApplicationNode applicationNode);
    List<GenericApplicationNode> fetchApplicationNodeByName(String name);
}
