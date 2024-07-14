package com.pinecone.slime.jelly.source.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class RedisIterator implements Iterator<Object > {
    private final Jedis             mJedis;
    private final ScanParams        mScanParams;
    private String                  mCursor;
    private List<? >                mCurrentBatch;
    private int                     mCurrentIndex;
    private IteratorSourceAdapter   mSourceAdapter;

    public RedisIterator( Jedis jedis, String namespace, int batchSize, IteratorSourceAdapter adapter ) {
        this.mJedis          = jedis;
        this.mScanParams     = new ScanParams().match( namespace + "*" ).count( batchSize );
        this.mCursor         = ScanParams.SCAN_POINTER_START;
        this.mCurrentBatch   = null;
        this.mCurrentIndex   = 0;
        this.mSourceAdapter  = adapter;
        this.fetchNextBatch();
    }

    public RedisIterator(Jedis jedis, String namespace, IteratorSourceAdapter adapter ) {
        this( jedis, namespace, 1000, adapter );
    }

    private void fetchNextBatch() {
        ScanResult<? > scanResult      = this.mSourceAdapter.scan( this.mCursor, this.mScanParams );
        this.mCurrentBatch             = scanResult.getResult();
        this.mCursor                   = scanResult.getCursor();
        this.mCurrentIndex             = 0;
    }

    @Override
    public boolean hasNext() {
        if ( this.mCurrentBatch == null || this.mCurrentIndex >= this.mCurrentBatch.size() ) {
            if ( this.mCursor.equals( ScanParams.SCAN_POINTER_START ) ) {
                return false;
            }
            this.fetchNextBatch();
        }
        return this.mCurrentIndex < this.mCurrentBatch.size();
    }

    @Override
    public Object next() {
        if ( !this.hasNext() ) {
            throw new NoSuchElementException();
        }
        return this.mCurrentBatch.get( this.mCurrentIndex++ );
    }
}