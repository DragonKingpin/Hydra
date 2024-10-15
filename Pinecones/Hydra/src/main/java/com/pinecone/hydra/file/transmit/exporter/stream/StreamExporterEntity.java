package com.pinecone.hydra.file.transmit.exporter.stream;

import com.pinecone.hydra.file.transmit.exporter.ExporterEntity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamExporterEntity extends ExporterEntity {
    OutputStream getOutputStream();
    void setOutputStream( OutputStream outputStream);
    void export() throws IOException;

    @Override
    default StreamExporterEntity evinceStreamExporterEntity() {
        return this;
    }
}
