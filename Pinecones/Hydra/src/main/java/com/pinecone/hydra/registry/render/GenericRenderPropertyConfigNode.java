package com.pinecone.hydra.registry.render;

import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.registry.RenderDistributeRegistry;
import com.pinecone.hydra.registry.entity.GenericConfigNodeMeta;
import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.entity.GenericPropertyConfigNode;
import com.pinecone.hydra.registry.entity.Property;

import java.util.List;
import java.util.Set;

public class GenericRenderPropertyConfigNode extends GenericPropertyConfigNode implements RenderPropertyConfigNode {
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
    public List<GenericProperty> getProperties() {
        return super.getProperties();
    }


    @Override
    public RenderDistributeRegistry getRegistry() {
        return this.registry;
    }
}
