package com.pinecone.hydra.registry.entity;

import java.util.List;
import java.util.Set;

public interface PropertiesNode extends ConfigNode {

    List<GenericProperty > getProperties();

    void setProperties(List<GenericProperty> properties);

    void put             ( String key, Object val );

    void putProperty     ( Property property );

    void remove          ( String key );

    void update          ( Property property );

    Property get         ( String key );

    Object getValue      ( String key );

    boolean containsKey  ( String key );

    int size();

    boolean isEmpty();

    List<Object > values();

    Set<String > keySet();

    Set<Property > entrySet();

    @Override
    default PropertiesNode evincePropertyConfig() {
        return this;
    }
}
