package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;

import java.util.List;

public class PropertyConfNodeOperator extends ArchConfigNodeOperator {
    protected RegistryPropertiesManipulator registryPropertiesManipulator;
    public PropertyConfNodeOperator(ConfigOperatorFactory factory) {
        super(factory);
        this.registryPropertiesManipulator=factory.getMasterManipulator().getRegistryPropertiesManipulator();
    }

    @Override
    public Properties get(GUID guid) {
        //检测缓存中是否存在信息
        Properties configNode = (Properties)this.cacheMap.get(guid);
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
    protected Properties getConfigNodeWideData(GUID guid ) {
        ConfigNode configNodeWideData = super.getConfigNodeWideData( guid );
        if( configNodeWideData instanceof Properties) {
            List<Property > properties = this.registryPropertiesManipulator.getProperties( guid );
            Properties propertiesNode = (Properties) configNodeWideData;
            propertiesNode.setProperties( properties );
            return propertiesNode;
        }

        throw new IllegalStateException(
                String.format(
                        "'%s' should be `PropertiesNode` be `%s` found.",
                        guid.toString(), configNodeWideData.getClass().getSimpleName()
                )
        );
    }
}
