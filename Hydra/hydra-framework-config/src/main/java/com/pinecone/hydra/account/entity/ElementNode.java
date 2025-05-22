package com.pinecone.hydra.account.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.system.ko.meta.ElementObject;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

public interface ElementNode extends TreeNode, ElementObject {
    long getEnumId();
    void setEnumId( long enumId );

    String getName();
    void setName( String name );

    GUID getGuid();
    void setGuid( GUID guid );

    @Override
    default String getObjectCategoryName() {
        return "Account";
    }
}
