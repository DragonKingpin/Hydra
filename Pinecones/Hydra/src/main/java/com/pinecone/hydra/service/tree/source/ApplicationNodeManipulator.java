package com.pinecone.hydra.service.tree.source;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.tree.nodes.GenericApplicationNode;

import java.util.List;

public interface ApplicationNodeManipulator {
    //ApplicationNode的CRUD
    void saveApplicationNode(GenericApplicationNode applicationNode);
    void deleteApplicationNode(GUID UUID);
    GenericApplicationNode selectApplicationNode(GUID UUID);
    void updateApplicationNode(GenericApplicationNode applicationNode);
    List<GenericApplicationNode> selectApplicationNodeByName(String name);
}
