package com.pinecone.hydra.registry.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.Registry;

public interface ElementNode extends RegistryTreeNode {
    long getEnumId();

    GUID getGuid();

    LocalDateTime getCreateTime();

    LocalDateTime getUpdateTime();

    String getName();

    Attributes getAttributes();

    Registry parentRegistry();
}
