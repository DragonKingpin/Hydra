package com.pinecone.hydra.registry.entity;

import java.time.LocalDateTime;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.Registry;
import com.pinecone.hydra.system.ko.meta.ElementObject;

public interface ElementNode extends RegistryTreeNode, ElementObject {
    long getEnumId();

    GUID getGuid();

    LocalDateTime getCreateTime();

    LocalDateTime getUpdateTime();

    String getName();

    Attributes getAttributes();

    Registry parentRegistry();

    @Override
    default String getObjectCategoryName() {
        return "Registry";
    }
}
