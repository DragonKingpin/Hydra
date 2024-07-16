package com.pinecone.slime.jelly.source.redis;

import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisEntryIterator extends RedisIterator {
    public RedisEntryIterator( Jedis jedis, String namespace, int batchSize, IteratorSourceAdapter adapter ) {
        super( jedis, namespace, batchSize, adapter );
    }

    public RedisEntryIterator( Jedis jedis, String namespace, IteratorSourceAdapter adapter ) {
        this( jedis, namespace, 1000, adapter );
    }

    @Override
    @SuppressWarnings( "unchecked" )
    public Map.Entry<String, String > next() {
        return (Map.Entry<String, String >) super.next();
    }
}
