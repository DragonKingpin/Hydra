package com.pinecone.hydra.registry.render;

import com.pinecone.hydra.registry.RenderDistributeRegistry;
import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.entity.GenericPropertiesNode;
import com.pinecone.hydra.registry.entity.Property;

import java.util.List;
import java.util.Set;

public class GenericRenderPropertiesNode extends GenericPropertiesNode implements RenderPropertiesNode {
    protected RenderDistributeRegistry       registry;
    public void apply(RenderDistributeRegistry registry) {
       this.registry = registry;
    }

    @Override
    public RenderProperty get(String key) {
        for(Property p : this.properties){
            if (p.getKey().equals(key)){
                return (GenericRenderProperty) p;
            }
        }
        return null;
    }

    @Override
    public Set<Property> entrySet() {
        return super.entrySet();
    }

    @Override
    public List<Property > getProperties() {
        return super.getProperties();
    }


    @Override
    public RenderDistributeRegistry getRegistry() {
        return this.registry;
    }
}
