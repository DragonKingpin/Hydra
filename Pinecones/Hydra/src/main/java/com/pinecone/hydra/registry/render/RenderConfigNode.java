package com.pinecone.hydra.registry.render;

import com.pinecone.framework.util.template.UTRAlmondProvider;
import com.pinecone.framework.util.template.UniformTemplateRenderer;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.Property;

public interface RenderConfigNode extends ConfigNode {
    default Object renderValue      ( String key, UniformTemplateRenderer renderer, Object tpl ) {
        Property v = this.getProperty( key );
        if( v.isStringBasedType() ) {
            return renderer.render( v.getValue().toString(), tpl );
        }
        return v.getValue().toString();
    }

//    default Object renderValue      ( String key, Object tpl ) {
//        Property v = this.getProperty( key );
//        if( v.isStringBasedType() ) {
//            return this.getRegistry().getRenderer().render( v.getValue().toString(), tpl );
//        }
//        return v.getValue().toString();
//    }
}
