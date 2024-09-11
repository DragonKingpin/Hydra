package com.walnut.sparta.services.mapper;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.scenario.entity.GenericNamespaceNodeMeta;
import com.pinecone.hydra.scenario.entity.NamespaceNodeMeta;
import com.pinecone.hydra.scenario.source.NamespaceNodeMetaManipulator;

public interface ScenarioNamespaceNodeMetaMapper extends NamespaceNodeMetaManipulator {
    void insert(NamespaceNodeMeta namespaceNodeMeta);

    void remove(GUID guid);

    GenericNamespaceNodeMeta getNamespaceNodeMeta(GUID guid);

    void update(NamespaceNodeMeta namespaceNodeMeta);
}
