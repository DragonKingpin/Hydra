package com.pinecone.hydra.registry.operator;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.ConfigNode;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.Property;
import com.pinecone.hydra.registry.source.RegistryPropertiesManipulator;

import java.util.List;

public class PropertiesOperator extends ArchConfigNodeOperator {
    protected RegistryPropertiesManipulator registryPropertiesManipulator;

    public PropertiesOperator( ConfigOperatorFactory factory ) {
        super(factory);
        this.registryPropertiesManipulator=factory.getMasterManipulator().getRegistryPropertiesManipulator();
    }

    @Override
    public Properties get( GUID guid ) {
        return (Properties) super.get( guid );
    }

    @Override
    protected Properties getConfigNodeWideData( GUID guid ) {
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

    @Override
    public void purge( GUID guid ) {
        super.purge(guid);
        this.registryPropertiesManipulator.removeAll(guid);
    }


}