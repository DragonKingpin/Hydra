package com.pinecone.hydra.file;

import com.pinecone.framework.system.prototype.Pinenut;

public interface FrameSegmentNaming extends Pinenut {
    String naming( String fileName, long segId, String crc3 );
}
