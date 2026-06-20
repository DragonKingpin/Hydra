package com.pinecone.hydra.storage.file.delete;

import com.pinecone.framework.system.prototype.Pinenut;

@FunctionalInterface
public interface UofsPathCacheEraser extends Pinenut {
    void erase( String path );
}
