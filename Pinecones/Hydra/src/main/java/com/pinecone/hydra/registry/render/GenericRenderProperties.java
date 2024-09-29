package com.pinecone.hydra.registry.render;

import com.pinecone.hydra.registry.RenderDistributeRegistry;
import com.pinecone.hydra.registry.entity.GenericProperties;
import com.pinecone.hydra.registry.entity.Property;

import java.util.Collection;
import java.util.Set;

public class GenericRenderProperties extends GenericProperties implements RenderProperties {
    protected RenderDistributeRegistry       registry;
    public void apply(RenderDistributeRegistry registry) {
       this.registry = registry;
    }

    @Override
    public RenderProperty get(String key) {
        return (RenderProperty)super.get( key );
    }

    @Override
    public Set<Property> entrySet() {
        return super.entrySet();
    }

    @Override
    public Collection<Property > getProperties() {
        return super.getProperties();
    }


    @Override
    public RenderDistributeRegistry getRegistry() {
        return this.registry;
    }
}
