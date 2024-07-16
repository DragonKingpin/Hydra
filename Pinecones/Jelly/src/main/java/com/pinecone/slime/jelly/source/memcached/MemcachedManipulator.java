package com.pinecone.slime.jelly.source.memcached;

import com.pinecone.framework.unit.Units;
import com.pinecone.slime.source.indexable.IndexableIterableManipulator;
import net.spy.memcached.MemcachedClient;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;

public interface MemcachedManipulator<K extends String, V extends Serializable> extends IndexableIterableManipulator<K, V > {
    MemcachedClient getClient();

    default Collection<String > keys( Class<? extends Collection > stereo ) {
        try{
            MemcachedClient client = this.getClient();

            Collection<String > allKeys = Units.newInstance( stereo );
            Map<SocketAddress, Map<String, String > > items = client.getStats( "items" );

            for ( Map.Entry<SocketAddress, Map<String, String > > entry : items.entrySet() ) {
                Map<String, String > itemMap = entry.getValue();
                for ( String key : itemMap.keySet() ) {
                    if ( key.startsWith("items:") ) {
                        String[] parts = key.split(":");
                        if ( parts.length > 2 && "number".equals(parts[2]) ) {
                            int slabNumber = Integer.parseInt(parts[1]);
                            int limit = Integer.parseInt( itemMap.get(key) );
                            Map<SocketAddress, Map<String, String> > dump = client.getStats( "cachedump " + slabNumber + " " + limit );
                            for  ( Map<String, String > dumpMap : dump.values() ) {
                                allKeys.addAll( dumpMap.keySet() );
                            }
                        }
                    }
                }
            }

            return allKeys;
        }
        catch ( IllegalArgumentException e ) {
            return this.keys();
        }
    }

    default Collection<String > keys() {
        return this.keys( ArrayList.class );
    }

    default Collection<String > keySet() {
        return this.keys( LinkedHashSet.class );
    }
}
