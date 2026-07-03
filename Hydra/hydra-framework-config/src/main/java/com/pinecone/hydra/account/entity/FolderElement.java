package com.pinecone.hydra.account.entity;

import com.pinecone.framework.util.id.GUID;

import java.util.Collection;

public interface FolderElement extends ElementNode {
    Collection<ElementNode> fetchChildren();

    Collection<GUID> fetchChildrenGuids();

    void addChild( ElementNode child );

    boolean containsChild( String childName );
}
