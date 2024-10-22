package com.pinecone.hydra.storage.file;

import com.pinecone.framework.system.prototype.Pinenut;

public interface FrameSegmentNaming extends Pinenut {
    String naming( String fileName, long segId, String crc3 );
}
