package com.pinecone.slime.jelly.source.redis;

import redis.clients.jedis.Jedis;

import java.util.Map;

public class RedisKeysIterator extends RedisIterator {
    public RedisKeysIterator( Jedis jedis, String namespace, int batchSize, IteratorSourceAdapter adapter ) {
        super( jedis, namespace, batchSize, adapter );
    }

    public RedisKeysIterator( Jedis jedis, String namespace, IteratorSourceAdapter adapter ) {
        this( jedis, namespace, 1000, adapter );
    }

    @Override
    public String next() {
        Object e = super.next();
        if( e instanceof String ) {
            return ( String ) e;
        }
        else {
            Map.Entry entry = (Map.Entry) e;
            return (String) entry.getKey();
        }
    }
}