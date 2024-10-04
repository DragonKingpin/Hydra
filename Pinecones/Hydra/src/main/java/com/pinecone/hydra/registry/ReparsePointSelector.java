package com.pinecone.hydra.registry;

import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;

public interface ReparsePointSelector extends PathSelector {
    ReparseLinkNode searchLinkNode( String[] parts );

    Object search( String[] parts );
}
