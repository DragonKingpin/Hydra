package com.pinecone.hydra.storage.file.external;

import com.pinecone.hydra.storage.UFile;

import java.io.File;
import java.net.URI;

public interface ExternalFile extends ExternalFileObject, UFile {

    File getNativeFile();

    URI toURI();

    String getName();

    String getPath();

    boolean delete();

    default boolean exists() {
        return this.getNativeFile().exists();
    }

    @Override
    default Object getNativeHandle() {
        return this.getNativeFile();
    }

}
