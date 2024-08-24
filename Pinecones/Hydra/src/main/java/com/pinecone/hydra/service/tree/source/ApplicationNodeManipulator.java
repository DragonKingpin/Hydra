package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;

import java.util.List;

public interface ApplicationNodeManipulator {
    //ApplicationNode的CRUD
    void insertApplicationNode(GenericApplicationNode applicationNode);
    void deleteApplicationNode(GUID guid);
    GenericApplicationNode getApplicationNode(GUID guid);
    void updateApplicationNode(GenericApplicationNode applicationNode);
    List<GenericApplicationNode> fetchApplicationNodeByName(String name);
}
