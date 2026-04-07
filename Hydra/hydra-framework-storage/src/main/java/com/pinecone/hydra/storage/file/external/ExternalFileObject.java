package com.pinecone.hydra.storage.file.external;

import java.net.URI;

import com.pinecone.hydra.storage.file.entity.ElementNode;

public interface ExternalFileObject extends ElementNode {

    URI toURI();

    @Override
    String getName();

    String getPath();

    boolean delete();

    Object getNativeHandle();

}
