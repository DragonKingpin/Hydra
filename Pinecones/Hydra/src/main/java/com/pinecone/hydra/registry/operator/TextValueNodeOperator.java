package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.GenericConfigNodeMeta;
import com.pinecone.hydra.registry.entity.GenericNodeCommonData;
import com.pinecone.hydra.registry.entity.GenericTextConfigNode;
import com.pinecone.hydra.registry.entity.TextConfigNode;
import com.pinecone.hydra.registry.entity.TextValue;
import com.pinecone.hydra.registry.source.RegistryTextValueManipulator;

public class TextValueNodeOperator extends ArchRegistryNodeOperator{
    protected RegistryTextValueManipulator registryTextValueManipulator;
    public TextValueNodeOperator(ConfigOperatorFactory factory) {
        super(factory);
        this.registryTextValueManipulator = factory.getMasterManipulator().getRegistryTextValueManipulator();
    }

    @Override
    public TextConfigNode get(GUID guid) {
        //检测缓存中是否存在信息
        TextConfigNode configNode = (TextConfigNode)this.cacheMap.get(guid);
        if (configNode == null) {
            configNode = this.getConfigNodeWideData(guid);
            GUID parentGuid = configNode.getParentGuid();
            if (parentGuid != null){
                this.inherit(configNode,get(parentGuid));
            }
            this.cacheMap.put(guid, configNode);
        }
        return configNode;
    }


    @Override
    protected TextConfigNode getConfigNodeWideData(GUID guid) {
        ConfigNode configNodeWideData = super.getConfigNodeWideData(guid);
        GenericTextConfigNode textConfNode = new GenericTextConfigNode();
        TextValue textValue = this.registryTextValueManipulator.getTextValue(guid);
        textConfNode.setTextValue(textValue);
        textConfNode.setConfigNodeMeta((GenericConfigNodeMeta) configNodeWideData.getConfigNodeMeta());
        textConfNode.setNodeCommonData((GenericNodeCommonData) configNodeWideData.getNodeCommonData());
        textConfNode.setGuid(configNodeWideData.getGuid());
        textConfNode.setName(configNodeWideData.getName());
        textConfNode.setCreateTime(configNodeWideData.getCreateTime());
        textConfNode.setRegistry(configNodeWideData.getRegistry());
        textConfNode.setUpdateTime(configNodeWideData.getUpdateTime());
        return textConfNode;
    }
}
