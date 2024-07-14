package com.pinecone.slime.cache.query;

import com.pinecone.framework.unit.LinkedTreeMap;
import com.pinecone.framework.unit.MapDictium;

import java.util.LinkedHashMap;
import java.util.Map;

public class LocalFixedLRUDictCachePage<V > extends ArchLocalDictCachePage<V > implements UniformCountSelfLoadingDictCache<V > {
    protected static <V > Map<Object, V > newMap( boolean bUsingTree, int capacity, Map<Object, V > initData ) {
        Map<Object, V > neo;

        if( bUsingTree ) {
            neo = new LinkedTreeMap<>( true ){
                @Override
                protected boolean removeEldestEntry( Map.Entry<Object, V > eldest ) {
                    return this.size() > capacity;
                }
            };
        }
        else {
            neo = new LinkedHashMap<>( capacity, 0.75f, true ){
                @Override
                protected boolean removeEldestEntry( Map.Entry<Object, V > eldest ) {
                    return this.size() > capacity;
                }
            };
        }

        if( initData != null ) {
            if( initData.size() > capacity ) {
                throw new IllegalArgumentException( String.format( "The initialization size[%d] exceeds the capacity[%d].", initData.size(), capacity ) );
            }
            neo.putAll( initData );
        }
        return neo;
    }

    private SourceRetriever<V > mSourceRetriever;

    public LocalFixedLRUDictCachePage( long id, int capacity, boolean bUsingTree, Map<Object, V > initData, SourceRetriever<V > retriever ) {
        super( id, capacity, new MapDictium<>( LocalFixedLRUDictCachePage.newMap( bUsingTree, capacity, initData ) ) );

        this.mSourceRetriever = retriever;
    }

    public LocalFixedLRUDictCachePage( int capacity, Map<Object, V > initData, SourceRetriever<V > retriever ) {
        this( -1, capacity, false, initData, retriever );
    }

    public LocalFixedLRUDictCachePage( int capacity, SourceRetriever<V > retriever ) {
        this(  capacity, null, retriever );
    }

    @Override
    protected void afterKeyVisited( Object key ) {
        super.afterKeyVisited( key );
        // Since we used the `accessOrder`, the newest accessed key will auto moving to the top.
    }

    @Override
    protected V missKey( Object key ) {
        this.recordMiss();
        V v = this.mSourceRetriever.retrieve( key );
        if( v != null ) {
            this.getDictium().insert( key, v );
        }
        return v;
    }

    @Override
    public boolean implicatesKey( Object key ) {
        return this.get( key ) != null;
    }

    @Override
    public SourceRetriever<V > getSourceRetriever() {
        return this.mSourceRetriever;
    }
}
