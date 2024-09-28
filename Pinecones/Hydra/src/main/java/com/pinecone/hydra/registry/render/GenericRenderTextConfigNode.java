package com.pinecone.hydra.registry.render;

import com.pinecone.hydra.registry.DistributedRegistry;
import com.pinecone.hydra.registry.RenderDistributeRegistry;
import com.pinecone.hydra.registry.entity.GenericTextConfigNode;
import com.pinecone.hydra.registry.entity.TextValue;

public class GenericRenderTextConfigNode extends GenericTextConfigNode implements RenderTextConfigNode {
    protected RenderDistributeRegistry registry;

    public void apply(RenderDistributeRegistry registry) {
        super.apply(registry);
    }
    @Override
    public RenderTextValue get() {
        return (RenderTextValue) this.textValue;
    }

    @Override
    public DistributedRegistry getRegistry() {
        return super.getRegistry();
    }
}
