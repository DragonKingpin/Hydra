package com.pinecone.hydra.registry.render;

import com.pinecone.hydra.registry.RenderDistributeRegistry;
import com.pinecone.hydra.registry.entity.GenericNamespaceNode;

public class GenericRenderNamespaceNode extends GenericNamespaceNode implements RenderNamespaceNode {
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
    public RenderNamespaceNode getNamespaceNode(String key) {
        return (RenderNamespaceNode) this.children.get(key);
    }
}
