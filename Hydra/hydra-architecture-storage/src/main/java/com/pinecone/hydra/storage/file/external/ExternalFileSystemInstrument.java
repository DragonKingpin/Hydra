package com.pinecone.hydra.storage.file.external;

import com.pinecone.framework.system.regime.Instrument;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;

import java.io.IOException;

public interface ExternalFileSystemInstrument extends Instrument {

    void insertExternalSymbolic( ExternalSymbolic externalSymbolic );

    void createExternalSymbolic( String folderPath, String externalSymbolicName,String reparsedPoint );

    ElementNode queryElement( String path );

    ExternalFolder affirmFolder( String path );

    boolean remove( String path );

    void copy( String sourcePath, String destinationPath ) throws IOException;

}
