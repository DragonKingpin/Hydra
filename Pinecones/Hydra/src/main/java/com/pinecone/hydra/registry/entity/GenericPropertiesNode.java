package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSONArray;
import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
public class GenericPropertiesNode extends ArchConfigNode implements PropertiesNode {
    protected List<Property >     properties;

    public GenericPropertiesNode() {
    }

    public GenericPropertiesNode( DistributedRegistry registry ) {
        super( registry );
    }

    public GenericPropertiesNode(
            int enumId, GUID guid, GUID nsGuid, GUID parentGuid, LocalDateTime createTime, LocalDateTime updateTime,
            String name, List<Property> properties, GenericConfigNodeMeta configNodeMeta,
            GenericNodeCommonData nodeCommonData, DistributedRegistry registry
    ) {
        super( registry, enumId, guid, nsGuid, parentGuid, createTime, updateTime, name, configNodeMeta, nodeCommonData );
        this.properties = properties;
    }


    @Override
    public void put( String key, Object val ) {
        Property p = new GenericProperty();
        p.setKey( key );
        p.setGuid( this.guid );

        if( val != null ) {
            p.setValue( val.toString() );
        }
        String type = PropertyTypes.queryType( val );
        p.setType( type );
        p.setCreateTime( LocalDateTime.now() );
        p.setUpdateTime( LocalDateTime.now() );

        this.putProperty( p );
    }

    @Override
    public void putProperty( Property property ) {
        boolean isContain= this.containsKey(property.getKey());
        if (isContain) {
            for(Property p : this.properties){
                if (p.getKey().equals(property.getKey())){
                    p.setValue(property.getValue());
                    p.setType(property.getType());
                }
            }
            this.registry.updateProperty( property, this.guid );
        }
        else {
            this.properties.add( (GenericProperty) property );
            this.registry.putProperties( property, this.guid );
        }

    }

    @Override
    public void remove( String key ) {
        this.properties.remove(key);
        this.registry.removeProperty( this.guid, key );
    }

    @Override
    public void update( Property property ) {
        for(Property p : this.properties){
            if (p.getKey().equals(property.getKey())){
                p.setValue(property.getValue());
                p.setType(property.getType());
            }
        }
        this.registry.updateProperty( property, this.guid );
    }

    @Override
    public Property get( String key ) {
        for(Property p : this.properties){
            if (p.getKey().equals(key)){
                return p;
            }
        }
        return null;
    }

    @Override
    public Object getValue( String key ) {
        Property property = this.get( key );
        if( property != null ) {
            return property.getValue();
        }
        return null;
    }

    @Override
    public boolean containsKey( String key ) {
        for( Property p : this.properties ){
            if (p.getKey().equals(key)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.properties.size();
    }

    @Override
    public boolean isEmpty() {
        return !this.properties.isEmpty();
    }

    @Override
    public List<Object > values() {
        ArrayList<Object > values = new ArrayList<>();
        for( Property p : this.properties ){
            values.add(p.getValue());
        }
        return values;
    }

    @Override
    public Set<String > keySet() {
        HashSet<String > keys = new HashSet<>();
        for ( Property p : this.properties ){
            keys.add( p.getKey() );
        }
        return keys;
    }

    @Override
    public Set<Property > entrySet() {
        HashSet<Property> propertyHashSet = new HashSet<>();
        for( Property p : this.properties ){
            propertyHashSet.add(p);
        }
        return propertyHashSet;
    }

    @Override
    public List<Property> getProperties() {
        return this.properties;
    }

    @Override
    public Map<String, Object > toJSON() {
        Map<String, Object > jo = new LinkedHashMap<>();
        for( Property property : this.properties ) {
            jo.put( property.getKey(), property.getValue() );
        }
        return jo;
    }

    @Override
    public void setProperties( List<Property> properties ) {
        this.properties = properties;
    }

    @Override
    public DistributedRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public String toJSONString() {
        return BeanJSONEncoder.BasicEncoder.encode( this );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }
}
