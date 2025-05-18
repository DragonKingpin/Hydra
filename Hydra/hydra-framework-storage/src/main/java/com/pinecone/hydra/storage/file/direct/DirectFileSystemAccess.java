package com.pinecone.hydra.storage.file.direct;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;

import java.io.IOException;

public interface DirectFileSystemAccess extends Pinenut {

    ElementNode queryElement(String path);

    void insertExternalSymbolic( ExternalSymbolic externalSymbolic );

    void copy( String sourcePath, String destinationPath ) throws IOException;

    void createExternalSymbolic( String folderPath, String externalSymbolicName,String reparsedPoint );
}
