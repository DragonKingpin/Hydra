package com.pinecone.hydra.storage.file.transmit.exporter;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.Cluster;

import java.io.IOException;

public interface FileExport extends Pinenut {
    void export() throws IOException;

    void export(Cluster cluster) throws IOException;

    void export( Number offset, Number endSize ) throws  IOException;
}
