package com.pinecone.hydra.registry.marshaling;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.registry.entity.ElementNode;

public interface RegistryTransformer extends Pinenut {
    ElementNode decode( Object val );

    Object encode( ElementNode node );
}
