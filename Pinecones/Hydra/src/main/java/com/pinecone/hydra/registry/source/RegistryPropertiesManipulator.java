package com.pinecone.hydra.registry.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.registry.entity.GenericProperty;
import com.pinecone.hydra.registry.entity.Properties;
import com.pinecone.hydra.registry.entity.Property;

import java.util.List;

public interface RegistryPropertiesManipulator extends Pinenut {
    void insert(Property property);

    void remove( GUID guid, String key );

    List<Property > getProperties( GUID guid, Properties parent );

    void update( Property property );

    void removeAll( GUID guid );

    void copyPropertiesTo(GUID sourceGuid, GUID destinationGuid);
}
