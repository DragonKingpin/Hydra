package com.pinecone.hydra.storage.file.fat.io;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface VolumeSpaceAllocator extends Pinenut {
    long allocate( GUID volumeGuid, long lengthBytes );
}
