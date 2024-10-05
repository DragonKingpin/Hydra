package com.pinecone.hydra.registry.render;

import com.pinecone.framework.util.template.UniformTemplateRenderer;
import com.pinecone.hydra.registry.entity.TextFile;
import com.pinecone.hydra.registry.entity.TextValue;

public interface RenderTextFile extends TextFile,RenderRegistryTreeNode {
    default Object renderValue      ( UniformTemplateRenderer renderer, Object tpl ) {
        TextValue v = this.get( );
        return renderer.render( v.toString() ,tpl );
    }

    default Object renderValue      ( Object tpl ) {
        TextValue v = this.get();
        return this.getRegistry().getRenderer().render( v.toString(), tpl );

    }
}
