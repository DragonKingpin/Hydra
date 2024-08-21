package com.pinecone.hydra.unit.udsn;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.service.GenericApplicationNode;

import java.util.List;

public interface ApplicationNodeManipinate {
    //ApplicationNode的CRUD
    void saveApplicationNode(GenericApplicationNode applicationNode);
    void deleteApplicationNode(GUID UUID);
    GenericApplicationNode selectApplicationNode(GUID UUID);
    void updateApplicationNode(GenericApplicationNode applicationNode);
    List<GenericApplicationNode> selectApplicationNodeByName(String name);
}
