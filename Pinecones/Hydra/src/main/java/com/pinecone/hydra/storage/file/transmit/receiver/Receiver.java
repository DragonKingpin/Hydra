package com.pinecone.hydra.storage.file.transmit.receiver;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;

public interface Receiver extends Pinenut {
    void receive( ReceiveEntity entity ) throws IOException;
    void receive( ReceiveEntity entity, long offset,long endSize ) throws IOException;
    void resumableReceive(ReceiveEntity entity ) throws IOException;
}
