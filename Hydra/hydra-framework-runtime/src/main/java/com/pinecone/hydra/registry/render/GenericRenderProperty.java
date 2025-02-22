package com.pinecone.hydra.registry.render;

import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.entity.Properties;

public class GenericRenderProperty extends GenericProperty implements RenderProperty {
    public GenericRenderProperty() {
        super();
    }

    public GenericRenderProperty( Properties properties ) {
        super( properties );
    }
}
