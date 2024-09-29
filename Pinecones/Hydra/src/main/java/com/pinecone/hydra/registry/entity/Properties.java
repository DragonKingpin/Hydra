package com.pinecone.hydra.registry.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;

public interface Properties extends ConfigNode {

    Collection<Property > getProperties();

    Map<String, Object > toMap();

    default JSONObject toJSONObject() {
        return new JSONMaptron( this.toMap(), true );
    }

    void setProperties   ( List<Property> properties );

    void setProperties   ( Map<String, Property > properties );

    void put             ( String key, Object val );

    void put             ( Set<Map.Entry<String, Object > > entries );

    void putProperty     ( Property property );

    void remove          ( String key );

    void update          ( Property property );

    Property get         ( String key );

    Object getValue      ( String key );

    boolean containsKey  ( String key );

    int size();

    boolean isEmpty();

    Collection<Object > values();

    Set<String > keySet();

    Set<Property > entrySet();

    @Override
    default Properties evinceProperties() {
        return this;
    }
}
