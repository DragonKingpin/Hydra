package com.pinecone.hydra.registry.entity;

import com.pinecone.framework.util.json.hometype.BeanJSONEncoder;
import com.pinecone.hydra.registry.DistributedRegistry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
public class GenericProperties extends ArchConfigNode implements Properties {
    protected Map<String, Property >     properties;

    public GenericProperties() {
    }

    public GenericProperties(DistributedRegistry registry ) {
        super( registry );
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
    public void put( Set<Map.Entry<String, Object > > entries ) {
        for( Map.Entry<String, Object > kv : entries ) {
            this.put( kv.getKey(), kv.getValue() );
        }
    }

    @Override
    public void putProperty( Property property ) {
        boolean isContain = this.containsKey( property.getKey() );
        if ( isContain ) {
            Property p = this.get( property.getKey() );
            if( p != null ) {
                p.setValue(property.getValue());
                p.setType(property.getType());
            }

            this.registry.updateProperty( property, this.guid );
        }
        else {
            this.properties.put( property.getKey(), property );
            this.registry.putProperty( property, this.guid );
        }
    }

    @Override
    public void remove( String key ) {
        this.properties.remove(key);
        this.registry.removeProperty( this.guid, key );
    }

    @Override
    public void update( Property property ) {
        Property p = this.get( property.getKey() );
        if( p != null ) {
            p.setValue(property.getValue());
            p.setType(property.getType());
        }
        this.registry.updateProperty( property, this.guid );
    }

    @Override
    public Property get( String key ) {
        return this.properties.get( key );
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
        return this.properties.containsKey( key );
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
    public Collection<Object > values() {
        ArrayList<Object > values = new ArrayList<>();
        for( Property p : this.properties.values() ){
            values.add(p.getValue());
        }
        return values;
    }

    @Override
    public Set<String > keySet() {
        HashSet<String > keys = new HashSet<>();
        for ( Property p : this.properties.values() ){
            keys.add( p.getKey() );
        }
        return keys;
    }

    @Override
    public Set<Property > entrySet() {
        HashSet<Property> propertyHashSet = new HashSet<>();
        for( Property p : this.properties.values() ){
            propertyHashSet.add(p);
        }
        return propertyHashSet;
    }

    @Override
    public Collection<Property > getProperties() {
        return this.properties.values();
    }

    @Override
    public Map<String, Object > toMap() {
        Map<String, Object > jo = new LinkedHashMap<>();
        for( Property property : this.properties.values() ) {
            jo.put( property.getKey(), property.getValue() );
        }
        return jo;
    }

    @Override
    public void setProperties( List<Property> properties ) {
        this.properties = new LinkedHashMap<>();
        for( Property p : properties ) {
            this.properties.put( p.getKey(), p );
        }
    }

    @Override
    public void setProperties( Map<String, Property> properties ) {
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
