package com.pinecone.hydra.registry.entity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.unit.UniScopeMap;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONMaptron;
import com.pinecone.framework.util.json.JSONObject;

public interface Properties extends ConfigNode, PineUnit {

    Properties getParent();

    void setParent( Properties parent );

    Collection<Property > getProperties();

    Map<String, Object > toMap();

    default JSONObject toJSONObject() {
        return new JSONMaptron( this.toMap(), true );
    }

    UniScopeMap<String, Property > getPropertiesMap();

    void setProperties       ( List<Property> properties );

    void setProperties       ( UniScopeMap<String, Property > properties );

    void setThisProperties   ( Map<String, Property > properties );

    void setParentProperties ( UniScopeMap<String, Property > parent );

    Properties getOwner      ( String szKey );

    void put                 ( String key, Object val );

    void puts                ( Map<String, Object > map );

    void putProperty         ( Property property );

    void remove              ( String key );

    void update              ( Property property );

    void updateFromDummy     ( Property dummy );

    void set                 ( String key, Object val );

    Property get             ( String key );

    Object getValue          ( String key );

    boolean containsKey      ( String key );

    boolean containsKey      ( Object key );

    boolean hasOwnProperty   ( Object key );

    int size();

    boolean isEmpty();

    Collection<Object > values();

    Set<String > keySet();

    Set<Property > entrySet();

    @Override
    default Properties evinceProperties() {
        return this;
    }

    void copyValueTo(GUID destinationGuid );
    void copyTo    (GUID destinationGuid);
}
