package com.pinecone.framework.unit.multi;

import com.pinecone.framework.system.prototype.PineUnit;
import com.pinecone.framework.unit.MultiValueMapper;

import java.util.Map;
import java.util.Collection;

public interface MultiCollectionProxyMap<K, V, U extends Collection<V > > extends Map<K, U >, MultiValueMapper<K, V > {
    @Override
    default V erase( Object key, V value ) {
        Collection<V > more = this.get( key );

        if( more.size() == 1 ) {
            return this.remove( key ).iterator().next();
        }

        if( more.remove( value ) ){
            return value;
        }

        return null;
    }

    @Override
    default V get( Object k, V v ) {
        Collection<V > more = this.get( k );
        if( more.contains( v ) ){
            return v;
        }
        return null;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    default Collection<V > puts( K key, Collection<V > value ){
        return this.put( key, (U)value );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    default void putsAll( Map<? extends K, ? extends Collection<V > > m ) {
        this.putAll( (Map<? extends K, ? extends U >) m );
    }
}