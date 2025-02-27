package com.pinecone.hydra.registry.entity;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.util.id.GUID;

public interface Attributes extends PineUnit, Map<String, String > {
    GUID getGuid();

    void setGuid( GUID guid );

    String getAttribute( String key );

    void setAttribute( String key, String value );

    Map<String, String > getAttributes();

    void setAttributes( Map<String, String > attributes );

    ElementNode parentElement();

    @Override
    default boolean isEmpty() {
        return this.getAttributes().isEmpty();
    }

    @Override
    default int size() {
        return this.getAttributes().size();
    }

    @Override
    default boolean containsKey( Object key ) {
        return this.getAttributes().containsKey( key );
    }

    @Override
    default boolean hasOwnProperty( Object key ) {
        return this.containsKey( key );
    }

    @Override
    default boolean containsValue( Object value ) {
        return this.getAttributes().containsValue(value);
    }

    @Override
    default String get( Object key ) {
        return this.getAttributes().get(key);
    }

    @Override
    default Set<String > keySet() {
        return this.getAttributes().keySet();
    }

    @Override
    default Collection<String > values() {
        return this.getAttributes().values();
    }

    @Override
    default Set<Entry<String, String > > entrySet() {
        return this.getAttributes().entrySet();
    }


    String insert( String key, String value ) ;

    String update( String key, String value ) ;
}
