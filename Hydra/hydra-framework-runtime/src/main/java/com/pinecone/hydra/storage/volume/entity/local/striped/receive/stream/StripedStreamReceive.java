package com.pinecone.hydra.storage.volume.entity.local.striped.receive.stream;

import com.pinecone.hydra.storage.StorageIOResponse;
import com.pinecone.hydra.storage.io.UIOException;
import com.pinecone.hydra.storage.volume.entity.local.striped.receive.StripedReceiver;

public interface StripedStreamReceive extends StripedReceiver {
    StorageIOResponse streamReceive( ) throws UIOException;

    StorageIOResponse streamReceive( Number offset, Number endSize ) throws UIOException;
}
