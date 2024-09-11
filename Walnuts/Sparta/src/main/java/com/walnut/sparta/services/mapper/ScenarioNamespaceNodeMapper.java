package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.scenario.entity.GenericNamespaceNode;
import com.pinecone.hydra.scenario.entity.NamespaceNode;
import com.pinecone.hydra.scenario.source.NamespaceNodeManipulator;
import org.apache.ibatis.annotations.Select;

public interface ScenarioNamespaceNodeMapper extends NamespaceNodeManipulator {
    void insert(NamespaceNode namespaceNode);

    void remove(GUID guid);

    GenericNamespaceNode getNamespaceNode(GUID guid);

    void update(NamespaceNode namespaceNode);
}
