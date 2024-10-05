package com.pinecone.hydra.registry.render;

import com.pinecone.framework.util.template.UniformTemplateRenderer;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.entity.Properties;

public interface RenderProperties extends Properties, RenderRegistryTreeNode {
    default Object renderValue      ( String key, UniformTemplateRenderer renderer, Object tpl ) {
        Property v = this.get( key );
        if( v.isStringBasedType() ) {
            return renderer.render( v.getValue().toString(), tpl );
        }
        return v.getValue().toString();
    }

    default Object renderValue      ( String key, Object tpl ) {
        Property v = this.get( key );
        if( v.isStringBasedType() ) {
            return this.getRegistry().getRenderer().render( v.getValue().toString(), tpl );
        }
        return v.getValue().toString();
    }
}
