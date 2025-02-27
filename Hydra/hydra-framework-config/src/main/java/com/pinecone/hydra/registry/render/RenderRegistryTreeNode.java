package com.pinecone.hydra.registry.render;

import com.pinecone.hydra.registry.RenderDistributeRegistry;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;

public interface RenderRegistryTreeNode extends RegistryTreeNode {
    RenderDistributeRegistry getRegistry();
}
