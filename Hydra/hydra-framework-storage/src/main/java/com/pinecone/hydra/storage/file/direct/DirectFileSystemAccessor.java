package com.pinecone.hydra.storage.file.direct;

import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;

import java.io.IOException;

public interface DirectFileSystemAccessor extends DirectFileInstrument {

    ElementNode queryElement( String path );

    void copy( String sourcePath, String destinationPath ) throws IOException;

}
