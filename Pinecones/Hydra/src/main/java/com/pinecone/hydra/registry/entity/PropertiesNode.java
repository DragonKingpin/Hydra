package com.pinecone.hydra.registry.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;

public interface PropertiesNode extends ConfigNode {

    List<Property > getProperties();

    Map<String, Object > toJSON();

    default JSONObject toJSONObject() {
        return new JSONMaptron( this.toJSON(), true );
    }

    void setProperties   ( List<Property> properties );

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
