package com.pinecone.hydra.storage.file.transmit.exporter;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.Frame;
import com.pinecone.hydra.storage.io.UIOException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface FileExport extends Pinenut {
    void export() throws IOException;

    void export(Frame frame) throws IOException;

    void export( Number offset, Number endSize ) throws  IOException;
}
