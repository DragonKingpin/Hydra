package com.pinecone.hydra.storage.volume.entity.local.simple.recevice;

import com.pinecone.hydra.storage.io.Chanface;
import com.pinecone.hydra.storage.RandomAccessChanface;
import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.volume.entity.Receiver;
import com.pinecone.hydra.storage.volume.entity.local.striped.CacheBlock;

import java.io.IOException;

public interface SimpleReceive extends Receiver {
    StorageIOResponse receive(Chanface chanface,CacheBlock cacheBlock, byte[] buffer ) throws IOException;

    StorageIOResponse receive(RandomAccessChanface randomAccessChanface, CacheBlock cacheBlock, byte[] buffer ) throws IOException;
}
