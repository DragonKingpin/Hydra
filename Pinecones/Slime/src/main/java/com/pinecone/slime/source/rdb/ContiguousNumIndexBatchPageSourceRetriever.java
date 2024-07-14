package com.pinecone.slime.source.rdb;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.unit.MapDictium;
import com.pinecone.slime.cache.query.RangedDictCachePage;
import com.pinecone.slime.cache.query.pool.BatchPageSourceRetriever;
import com.pinecone.slime.cache.query.pool.LocalRangedDictCachePage;
import com.pinecone.slime.map.MonoKeyQueryRange;
import com.pinecone.slime.map.QueryRange;
import com.pinecone.slime.unitization.PartialRange;

import java.util.Map;

public class ContiguousNumIndexBatchPageSourceRetriever<K extends Number & Comparable<K >, V >  implements BatchPageSourceRetriever<V > {
    private   RangedRDBQuerierDataManipulator<K, V > mDataMapper;
    protected RDBTargetTableMeta                     mTableMeta;
    protected int                                    mnPageCapacity;
    protected String                                 mszRangeKey;

    public ContiguousNumIndexBatchPageSourceRetriever( RDBTargetTableMeta tableMeta, int nPageCapacity, String szRangeKey ) {
        this.mTableMeta     = tableMeta;
        this.mDataMapper    = (RangedRDBQuerierDataManipulator<K, V >) tableMeta.<K, V >getDataManipulator();
        this.mnPageCapacity = nPageCapacity;
        this.mszRangeKey    = szRangeKey;
    }

    @Override
    public long getBatchSize() {
        return this.mnPageCapacity;
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends Comparable<T > > T nextRangeMax( T key ) {
        if ( key instanceof Integer ) {
            return (T)(Integer)( (Integer) key + this.mnPageCapacity );
        }
        else if ( key instanceof Long ) {
            return (T)(Long)( (Long) key + this.mnPageCapacity );
        }
        else if ( key instanceof Short ) {
            return (T)(Short)( (Integer)( (Short) key + this.mnPageCapacity ) ).shortValue();
        }
        else if ( key instanceof Double ) {
            return (T)(Double)( (Double) key + this.mnPageCapacity );
        }
        else if ( key instanceof Float ) {
            return (T)(Float)( (Float) key + this.mnPageCapacity );
        }
        else if ( key instanceof Byte ) {
            return (T)(Byte)( (Integer)( (Byte) key + this.mnPageCapacity ) ).byteValue();
        }
        else {
            throw new IllegalArgumentException( "Unsupported number type." );
        }
    }

    @SuppressWarnings( "unchecked" )
    public QueryRange<K > queryRangeOnly( Object key ) {
        if ( key instanceof Integer ) {
            return (QueryRange<K >) this.calculateRangeForInteger( (Integer) key );
        }
        else if ( key instanceof Long ) {
            return (QueryRange<K >) this.calculateRangeForLong((Long) key);
        }
        else if ( key instanceof Short ) {
            return (QueryRange<K >) this.calculateRangeForShort((Short) key);
        }
        else if ( key instanceof Double ) {
            return (QueryRange<K >) this.calculateRangeForDouble((Double) key);
        }
        else if ( key instanceof Float ) {
            return (QueryRange<K >) this.calculateRangeForFloat((Float) key);
        }
        else if ( key instanceof Byte ) {
            return (QueryRange<K >) this.calculateRangeForByte((Byte) key);
        }
        else if ( key instanceof QueryRange ) {
            return (QueryRange<K >) key;
        }
        else {
            throw new IllegalArgumentException( "Unsupported number type." );
        }
    }

    protected QueryRange<? > calculateRangeForLong( Long key ) {
        long start = (key / this.mnPageCapacity) * this.mnPageCapacity;
        long end   = start + this.mnPageCapacity;
        return new MonoKeyQueryRange<>( start, end, this.mszRangeKey );
    }

    protected QueryRange<? > calculateRangeForInteger(Integer key ) {
        int start = (key / this.mnPageCapacity) * this.mnPageCapacity;
        int end   = start + this.mnPageCapacity;
        return new MonoKeyQueryRange<>( start, end, this.mszRangeKey );
    }

    protected QueryRange<? > calculateRangeForShort( Short key ) {
        short start = (short) ((key / this.mnPageCapacity) * this.mnPageCapacity);
        short end   = (short) (start + this.mnPageCapacity);
        return new MonoKeyQueryRange<>( start, end, this.mszRangeKey );
    }

    protected QueryRange<? > calculateRangeForDouble( Double key ) {
        double start = Math.floor(key / this.mnPageCapacity) * this.mnPageCapacity;
        double end   = start + this.mnPageCapacity;
        return new MonoKeyQueryRange<>( start, end, this.mszRangeKey );
    }

    protected QueryRange<? > calculateRangeForFloat( Float key ) {
        float start = (float) Math.floor(key / this.mnPageCapacity) * this.mnPageCapacity;
        float end   = start + this.mnPageCapacity;
        return new MonoKeyQueryRange<>( start, end, this.mszRangeKey );
    }

    protected QueryRange<? > calculateRangeForByte( Byte key ) {
        byte start = (byte) ((key / this.mnPageCapacity) * this.mnPageCapacity);
        byte end   = (byte) (start + this.mnPageCapacity);
        return new MonoKeyQueryRange<>( start, end, this.mszRangeKey );
    }

    @Override
    public String getRangeKey() {
        return this.mszRangeKey;
    }

    @Override
    public V retrieve( Object key ) {
        return this.mDataMapper.selectByKey( this.mTableMeta, key );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public <T extends Comparable<T > > RangedDictCachePage<V > retrieves( Object key, @Nullable PartialRange<T > range ) {
        QueryRange queryRange;
        if( range == null ) {
            queryRange = ( QueryRange )this.queryRangeOnly( key );
        }
        else {
            queryRange = new MonoKeyQueryRange<>( range.getMin(), range.getMax(), this.mszRangeKey );
        }

        Map map = this.mDataMapper.selectMappedByRange( this.mTableMeta, queryRange );
        return new LocalRangedDictCachePage<>( -1, this.mnPageCapacity, new MapDictium<>( map ), (PartialRange<T >)queryRange );
    }

    @Override
    public RangedDictCachePage<V > retrieves( Object key ) {
        return this.retrieves( key, null );
    }

    @Override
    public <T extends Comparable<T>> long counts( PartialRange<T> range ) {
        QueryRange queryRange;
        if( range instanceof QueryRange ) {
            queryRange = ( QueryRange ) range;
        }
        else {
            queryRange = new MonoKeyQueryRange<>( range.getMin(), range.getMax(), this.mszRangeKey );
        }
        return this.mDataMapper.countsByRange( this.mTableMeta, queryRange );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public long countsKey( Object key ) {
        if( key instanceof Comparable ) {
            return this.mDataMapper.countsByRange( this.mTableMeta, new MonoKeyQueryRange<>( (Comparable)key, (Comparable)key, this.mszRangeKey ) );
        }
        throw new IllegalArgumentException( "Key should be comparable." );
    }
}
