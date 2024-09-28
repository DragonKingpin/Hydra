package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.Debug;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.GenericConfigNodeMeta;
import com.pinecone.hydra.registry.entity.GenericNodeCommonData;
import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.entity.GenericPropertyConfigNode;
import com.pinecone.hydra.registry.entity.PropertyConfigNode;
import com.pinecone.hydra.registry.entity.RegistryTreeNode;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;

import java.util.List;

public class PropertyConfNodeOperator extends ArchRegistryNodeOperator{
    protected RegistryPropertiesManipulator registryPropertiesManipulator;
    public PropertyConfNodeOperator(ConfigOperatorFactory factory) {
        super(factory);
        this.registryPropertiesManipulator=factory.getMasterManipulator().getRegistryPropertiesManipulator();
    }

    @Override
    public PropertyConfigNode get(GUID guid) {
        //检测缓存中是否存在信息
        PropertyConfigNode configNode = (PropertyConfigNode)this.cacheMap.get(guid);
        if (configNode == null) {
            configNode = this.getConfigNodeWideData(guid);
            GUID parentGuid = configNode.getParentGuid();
            if (parentGuid != null){
                this.inherit(configNode, get(parentGuid));
            }
            this.cacheMap.put(guid, configNode);
        }
        return configNode;
    }

    @Override
    protected PropertyConfigNode getConfigNodeWideData(GUID guid) {
        ConfigNode configNodeWideData = super.getConfigNodeWideData(guid);
        GenericPropertyConfigNode PropertyConfigNode = new GenericPropertyConfigNode();
        List<GenericProperty> properties = this.registryPropertiesManipulator.getProperties(guid);
        PropertyConfigNode.setProperties(properties);
        PropertyConfigNode.setGuid(configNodeWideData.getGuid());
        PropertyConfigNode.setNodeCommonData((GenericNodeCommonData) configNodeWideData.getNodeCommonData());
        PropertyConfigNode.setConfigNodeMeta((GenericConfigNodeMeta) configNodeWideData.getConfigNodeMeta());
        PropertyConfigNode.setName(configNodeWideData.getName());
        PropertyConfigNode.setCreateTime(configNodeWideData.getCreateTime());
        PropertyConfigNode.setUpdateTime(configNodeWideData.getUpdateTime());
        PropertyConfigNode.setParentGuid(configNodeWideData.getParentGuid());
        return PropertyConfigNode;
    }
}
