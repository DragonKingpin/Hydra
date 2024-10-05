package com.pinecone.hydra.registry;

import com.pinecone.framework.util.template.UniformTemplateRenderer;

public interface RenderDistributeRegistry extends KOMRegistry {
    UniformTemplateRenderer getRenderer();
}
