package com.pinecone.hydra.registry.render;

import com.pinecone.hydra.registry.RenderDistributeRegistry;
import com.pinecone.hydra.registry.entity.GenericTextConfigNode;

public class GenericRenderTextConfigNode extends GenericTextConfigNode implements RenderTextConfigNode {
    protected RenderDistributeRegistry registry;

    public void apply(RenderDistributeRegistry registry) {
        super.apply(registry);
    }
    @Override
    public RenderTextValue get() {
        return (RenderTextValue) this.mTextValue;
    }

    @Override
    public RenderDistributeRegistry getRegistry() {
        return (RenderDistributeRegistry) super.getRegistry();
    }
}
