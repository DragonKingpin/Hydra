package com.pinecone.hydra.registry.entity;

import java.util.Map;

import com.pinecone.framework.unit.LinkedTreeMap;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.json.JSON;
import com.pinecone.hydra.registry.source.RegistryAttributesManipulator;

public class GenericAttributes implements Attributes {
    protected GUID                            guid;
    protected Map<String, String >            attributes = new LinkedTreeMap<>();
    protected ElementNode                     elementNode;
    protected RegistryAttributesManipulator   attributesManipulator;

    public GenericAttributes( GUID guid, ElementNode element, RegistryAttributesManipulator attributesManipulator ) {
        this.guid                  = guid;
        this.elementNode           = element;
        this.attributesManipulator = attributesManipulator;
    }


    @Override
    public GUID getGuid() {
        return this.guid;
    }

    @Override
    public void setGuid( GUID guid ) {
        this.guid = guid;
    }


    @Override
    public String getAttribute( String key ) {
        return this.attributes.get(key);
    }

    @Override
    public void setAttribute( String key, String value ) {
        this.put( key, value );
    }

    @Override
    public Map<String, String > getAttributes() {
        return this.attributes;
    }

    @Override
    public void setAttributes( Map<String, String > attributes ) {
        this.attributes = attributes;
        for( Map.Entry<String, String > kv : attributes.entrySet() ) {
            this.put( kv.getKey(), kv.getValue() );
        }
    }

    @Override
    public ElementNode parentElement() {
        return this.elementNode;
    }

    @Override
    public String toJSONString() {
        return JSON.stringify( this.attributes );
    }

    @Override
    public String toString() {
        return this.toJSONString();
    }

    @Override
    public void putAll( Map<? extends String, ? extends String> m ) {
        for( Map.Entry<? extends String, ? extends String> kv : m.entrySet() ) {
            this.put( kv.getKey(), kv.getValue() );
        }
    }

    @Override
    public String insert( String key, String value ) {
        if ( !this.attributesManipulator.containsKey( this.guid, key ) ) {
            this.attributesManipulator.insertAttribute( this.guid, key, value );
            this.attributes.put( key, value );
            return value;
        }
        return null;
    }

    @Override
    public String update( String key, String value ) {
        if ( !this.attributesManipulator.containsKey( this.guid, key ) ) {
            this.attributesManipulator.updateAttribute( this.guid, key, value );
            this.attributes.put( key, value );
            return value;
        }
        return null;
    }

    @Override
    public String put( String key, String value ) {
        if ( this.attributesManipulator.containsKey( this.guid, key ) ) {
            this.attributesManipulator.updateAttribute( this.guid, key, value );
        }
        else {
            this.attributesManipulator.insertAttribute( this.guid, key, value );
        }

        return this.attributes.put( key, value );
    }

    @Override
    public void clear() {
        this.attributesManipulator.clearAttributes( this.guid );
        this.attributes.clear();
    }

    @Override
    public boolean remove( Object key, Object value ) {
        if ( this.attributesManipulator.containsKey( this.guid, key.toString() ) ) {
            this.attributesManipulator.removeAttributeWithValue( this.guid, key.toString(), value.toString() );
            this.attributes.remove( key, value );
            return true;
        }
        return false;
    }

    @Override
    public String remove( Object key ) {
        if ( this.attributesManipulator.containsKey( this.guid, key.toString() ) ) {
            this.attributesManipulator.removeAttribute( this.guid, key.toString() );
            return this.attributes.remove( key );
        }
        return null;
    }


}
