package com.pinecone.hydra.registry.marshaling;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.registry.entity.ElementNode;

public interface RegistryEncoder extends Pinenut {
    Object encode( ElementNode node );
}
