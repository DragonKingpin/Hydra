package com.pinecone.tritium.ally.rdb;

import com.pinecone.slime.source.rdb.RDBClient;

public interface UniformRDBClient extends RDBClient {
    RDBManager getRDBManager();
}
