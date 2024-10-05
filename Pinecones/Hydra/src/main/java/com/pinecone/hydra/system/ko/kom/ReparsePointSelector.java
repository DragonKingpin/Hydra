package com.pinecone.hydra.system.ko.kom;

import com.pinecone.hydra.system.ko.kom.PathSelector;
import com.pinecone.hydra.unit.udtt.entity.ReparseLinkNode;

public interface ReparsePointSelector extends PathSelector {
    ReparseLinkNode searchLinkNode( String[] parts );

    Object search( String[] parts );
}
