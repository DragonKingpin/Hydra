package com.pinecone.hydra.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.framework.util.template.UTRAlmondProvider;
import com.pinecone.framework.util.template.UniformTemplateRenderer;
import com.pinecone.framework.util.uoi.UOI;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.registry.render.RenderConfigNode;
import com.pinecone.hydra.registry.render.RenderRegistryTreeNode;
import com.pinecone.hydra.registry.render.RenderTextValue;
import com.pinecone.hydra.system.Hydrarum;
import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;

import java.util.List;

public class GenericRenderDistributeRegistry extends GenericDistributeRegistry implements RenderDistributeRegistry {
    protected UniformTemplateRenderer       mUniformTemplateRenderer;
    private UniformTemplateRenderer         renderer;

    public GenericRenderDistributeRegistry(Hydrarum hydrarum, KOIMasterManipulator masterManipulator ){
        super( hydrarum, masterManipulator );
        this.renderer = new UTRAlmondProvider();
    }

    @Override
    public RenderRegistryTreeNode get(GUID guid) {
        return null;
    }

    @Override
    public RenderRegistryTreeNode getNodeByPath(String path) {
        return null;
    }

    @Override
    public RenderRegistryTreeNode getThis(GUID guid) {
        return (RenderRegistryTreeNode) this.getOperatorByGuid( guid ).getWithoutInheritance( guid );
    }

    @Override
    public RenderConfigNode getConfigNodeByGuid(GUID guid) {
        return (RenderConfigNode) super.getConfigNodeByGuid(guid);
    }

    @Override
    public RenderTextValue getTextValue(GUID guid) {
        return (RenderTextValue) this.registryTextValueManipulator.getTextValue(guid);
    }

    @Override
    public UniformTemplateRenderer getRenderer() {
        return this.renderer;
    }
}
