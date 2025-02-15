package com.pinecone.hydra.storage.file;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface ClusterSegmentNaming extends Pinenut {
    String naming( String fileName, long segId, String crc3 );

    String naming (String fileName, GUID frameGuid, int threadId );
}
