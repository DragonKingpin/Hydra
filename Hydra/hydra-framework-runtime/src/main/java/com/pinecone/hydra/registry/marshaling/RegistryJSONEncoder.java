package com.pinecone.hydra.registry.marshaling;

import com.pinecone.hydra.registry.KOMRegistry;
import com.pinecone.hydra.registry.entity.ElementNode;

public class RegistryJSONEncoder implements RegistryEncoder {
    protected KOMRegistry registry;

    public RegistryJSONEncoder( KOMRegistry registry ) {
        this.registry = registry;
    }

    @Override
    public Object encode( ElementNode node ) {
        if( node.evinceNamespace() != null ) {
            return node.evinceNamespace().toJSONObject();
        }
        else if( node.evinceProperties() != null ) {
            return node.evinceProperties().toJSONObject();
        }
        else if( node.evinceTextFile() != null ) {
            return node.evinceTextFile().toJSON();
        }
        return null;
    }

}
