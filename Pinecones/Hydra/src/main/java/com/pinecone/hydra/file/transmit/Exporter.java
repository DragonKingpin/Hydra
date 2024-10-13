package com.pinecone.hydra.file.transmit;

import com.pinecone.framework.system.prototype.Pinenut;

public interface Exporter extends Pinenut {
    void exporter( ExporterEntity entity );
}
