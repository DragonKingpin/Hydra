package com.pinecone.hydra.storage.file.fat.policy;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ChunkSizingPolicy extends Pinenut {
    long chooseChunkSize( long fileSize );
}
