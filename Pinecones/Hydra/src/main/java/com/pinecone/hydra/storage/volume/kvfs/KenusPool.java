package com.pinecone.hydra.storage.volume.kvfs;

import com.pinecone.framework.util.rdb.MappedExecutor;
import com.pinecone.framework.util.sqlite.SQLiteExecutor;
import com.pinecone.framework.util.sqlite.SQLiteHost;

import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KenusPool implements ExecutorPool {
    protected Map<String, MappedExecutor> kenusInstances;

    public KenusPool(){
        this.kenusInstances = new ConcurrentHashMap<>();
    }

    @Override
    public MappedExecutor allot(String name) {
        MappedExecutor mappedExecutor = this.kenusInstances.get(name);
        if( mappedExecutor == null ){
            try {
                SQLiteHost sqLiteHost = new SQLiteHost(name);
                SQLiteExecutor sqLiteExecutor = new SQLiteExecutor(sqLiteHost);
                this.kenusInstances.put(name, sqLiteExecutor);
                mappedExecutor = sqLiteExecutor;
            } catch (SQLException e) {
                 e.printStackTrace();
            }

        }
        return mappedExecutor;
    }
}
