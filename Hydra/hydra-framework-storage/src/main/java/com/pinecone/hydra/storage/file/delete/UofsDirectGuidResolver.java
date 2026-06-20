package com.pinecone.hydra.storage.file.delete;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

@FunctionalInterface
public interface UofsDirectGuidResolver extends Pinenut {
    GUID query( String path );
}
