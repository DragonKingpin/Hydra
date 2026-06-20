package com.pinecone.hydra.storage.file.external;

import java.io.File;

public class GenericNativeExternalFile extends ArchNativeExternalFileObject implements ExternalFile {

    public GenericNativeExternalFile( File file ) {
        super( file );
    }

    @Override
    public Number size() {
        return this.mNativeFile.length();
    }

}
