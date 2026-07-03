package com.pinecone.hydra.storage.file.delete;

import java.io.IOException;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.storage.file.entity.FileNode;

@FunctionalInterface
public interface UofsFileDataDeleter extends Pinenut {
    void delete( FileNode fileNode ) throws IOException;
}
