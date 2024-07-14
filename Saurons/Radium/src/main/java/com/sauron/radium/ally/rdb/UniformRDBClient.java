package com.sauron.radium.ally.rdb;

import com.pinecone.slime.source.rdb.RDBClient;

public interface UniformRDBClient extends RDBClient {
    RDBManager getRDBManager();
}
