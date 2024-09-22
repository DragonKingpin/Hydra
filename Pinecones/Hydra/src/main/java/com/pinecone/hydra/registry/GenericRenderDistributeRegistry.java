package com.pinecone.hydra.registry;

import com.pinecone.framework.util.template.UniformTemplateRenderer;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

public class GenericRenderDistributeRegistry extends GenericDistributeRegistry implements RenderDistributeRegistry {
    protected UniformTemplateRenderer mUniformTemplateRenderer;

    public GenericRenderDistributeRegistry( Hydrarum hydrarum, KOIMasterManipulator masterManipulator ){
        super( hydrarum, masterManipulator );
    }


    
}
