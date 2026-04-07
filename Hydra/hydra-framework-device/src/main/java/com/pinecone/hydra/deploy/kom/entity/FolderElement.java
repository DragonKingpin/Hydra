package com.pinecone.hydra.deploy.kom.entity;

import java.util.Collection;

import com.pinecone.framework.util.id.GUID;

public interface FolderElement extends ElementNode {

    Collection<ElementNode > fetchChildren();

    Collection<GUID > fetchChildrenGuids();

    void addChild( ElementNode child );

    boolean containsChild( String childName );

}
