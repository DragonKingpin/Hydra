package com.pinecone.hydra.registry.render;

import com.pinecone.hydra.registry.RenderDistributeRegistry;
import com.pinecone.hydra.registry.entity.GenericNamespace;

public class GenericRenderNamespace extends GenericNamespace implements RenderNamespace {
    protected RenderDistributeRegistry registry;

    public void apply( RenderDistributeRegistry registry ) {
        this.registry = registry;
    }

    @Override
    public RenderDistributeRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public RenderConfigNode getConfigNode(String key) {
        return (RenderConfigNode) this.children.get(key);
    }

    @Override
    public RenderNamespace getNamespace(String key) {
        return (RenderNamespace) this.children.get(key);
    }
}
