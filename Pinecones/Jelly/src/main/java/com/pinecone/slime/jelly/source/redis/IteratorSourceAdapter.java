package com.pinecone.slime.jelly.source.redis;

import com.pinecone.framework.system.prototype.Pinenut;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

public interface IteratorSourceAdapter extends Pinenut {
    ScanResult<? > scan( String cursor, ScanParams params );
}
