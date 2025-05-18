package com.pinecone.hydra.storage.file.direct;

import com.pinecone.hydra.storage.file.entity.FileTreeNode;

import java.io.File;
import java.net.URI;
import java.util.List;

public interface ExternalFolder extends ExternalFileObject {

    File getNativeFile();

    URI toURI();

    String getName();

    String getPath();

    String[] list();

    File[]   listFiles();

    List<FileTreeNode> listItem();

    boolean delete();

    @Override
    default Object getNativeHandler() {
        return this.getNativeFile();
    }

}
