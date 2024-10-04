package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.GenericConfigNodeMeta;
import com.pinecone.hydra.registry.entity.GenericNodeAttribute;
import com.pinecone.hydra.registry.entity.GenericTextConfigNode;
import com.pinecone.hydra.registry.entity.TextConfigNode;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.registry.source.RegistryTextValueManipulator;

public class TextValueNodeOperator extends ArchConfigNodeOperator {
    protected RegistryTextValueManipulator registryTextValueManipulator;
    public TextValueNodeOperator(ConfigOperatorFactory factory) {
        super(factory);
        this.registryTextValueManipulator = factory.getMasterManipulator().getRegistryTextValueManipulator();
    }

    @Override
    public TextConfigNode get(GUID guid) {
        return (TextConfigNode) super.get( guid );
    }

    @Override
    public TextConfigNode get( GUID guid, int depth ) {
        return this.get( guid );
    }

    @Override
    protected TextConfigNode getConfigNodeWideData(GUID guid) {
        ConfigNode configNodeWideData = super.getConfigNodeWideData(guid);
        GenericTextConfigNode textConfNode = new GenericTextConfigNode();
        TextValue textValue = this.registryTextValueManipulator.getTextValue(guid);
        textConfNode.setTextValue(textValue);
        textConfNode.setConfigNodeMeta((GenericConfigNodeMeta) configNodeWideData.getConfigNodeMeta());
        textConfNode.setNodeCommonData((GenericNodeAttribute) configNodeWideData.getNodeCommonData());
        textConfNode.setGuid(configNodeWideData.getGuid());
        textConfNode.setName(configNodeWideData.getName());
        textConfNode.setCreateTime(configNodeWideData.getCreateTime());
        textConfNode.setRegistry(configNodeWideData.getRegistry());
        textConfNode.setUpdateTime(configNodeWideData.getUpdateTime());
        return textConfNode;
    }

    @Override
    public void purge( GUID guid ) {
        super.purge(guid);
        this.registryTextValueManipulator.remove(guid);
    }


}
