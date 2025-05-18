package com.pinecone.hydra.storage.file.direct;

import com.pinecone.hydra.storage.UFile;
import com.pinecone.hydra.storage.file.entity.ElementNode;

import java.io.File;
import java.net.URI;

public interface ExternalFile extends ExternalFileObject, UFile {

    File getNativeFile();

    URI toURI();

    String getName();

    String getPath();

    boolean delete();

    @Override
    default Object getNativeHandler() {
        return this.getNativeFile();
    }

}
