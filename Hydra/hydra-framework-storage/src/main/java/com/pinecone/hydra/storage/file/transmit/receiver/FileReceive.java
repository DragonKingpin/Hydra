package com.pinecone.hydra.storage.file.transmit.receiver;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.volume.entity.LogicVolume;

import java.io.IOException;

public interface FileReceive extends Pinenut {
    void receive(LogicVolume volume) throws IOException;
    void receive( LogicVolume volume, long segId ) throws  IOException;
    void receive(LogicVolume volume, Number offset, Number endSize ) throws IOException;
    void randomReceive( LogicVolume volume, Number offset, Number endSize ) throws IOException;
}
