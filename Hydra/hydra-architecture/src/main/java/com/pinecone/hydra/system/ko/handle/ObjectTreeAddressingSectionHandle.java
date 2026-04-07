package com.pinecone.hydra.system.ko.handle;

import com.pinecone.hydra.unit.imperium.entity.EntityNode;

public interface ObjectTreeAddressingSectionHandle extends KHandle, SectionHandle {

    EntityNode queryNode( String path );

}
