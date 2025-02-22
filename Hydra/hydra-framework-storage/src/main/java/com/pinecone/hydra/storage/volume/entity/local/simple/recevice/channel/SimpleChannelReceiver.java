package com.pinecone.hydra.storage.volume.entity.local.simple.recevice.channel;

import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.entity.local.simple.recevice.SimpleReceiver;


import java.io.IOException;

public interface SimpleChannelReceiver extends SimpleReceiver {
    StorageIOResponse channelReceive( ) throws UIOException;
    StorageIOResponse channelReceive(Number offset, Number endSize) throws IOException;
}
