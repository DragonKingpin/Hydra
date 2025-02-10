package com.pinecone.hydra.storage.volume.entity.local.simple.recevice.stream;

import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.entity.local.simple.recevice.SimpleReceiver;
import com.pinecone.hydra.storage.volume.entity.local.striped.CacheBlock;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface SimpleStreamReceiver extends SimpleReceiver {
    StorageIOResponse streamReceive( ) throws UIOException;

    StorageIOResponse streamReceive(Number offset, Number endSize) throws UIOException;

    StorageIOResponse streamReceive( CacheBlock cacheBlock, byte[] buffer ) throws UIOException;
}
