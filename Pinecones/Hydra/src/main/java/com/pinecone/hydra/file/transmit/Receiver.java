package com.pinecone.hydra.file.transmit;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;

public interface Receiver extends Pinenut {
    void receive( ReceiveEntity entity ) throws IOException;
    void resumableTransfer( ReceiveEntity entity ) throws IOException;
}
