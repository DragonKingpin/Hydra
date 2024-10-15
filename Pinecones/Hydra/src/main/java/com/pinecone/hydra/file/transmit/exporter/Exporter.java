package com.pinecone.hydra.file.transmit.exporter;

import com.pinecone.framework.system.prototype.Pinenut;

import java.io.IOException;

public interface Exporter extends Pinenut {
    void export(ExporterEntity entity ) throws IOException;
    void resumablExport( ExporterEntity entity );
}
