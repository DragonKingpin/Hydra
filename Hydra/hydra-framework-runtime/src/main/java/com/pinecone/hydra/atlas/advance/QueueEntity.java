package com.pinecone.hydra.atlas.advance;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface QueueEntity extends Pinenut {
    void setGuid( GUID guid );

    GUID getGuid();

    void setStratum( int stratum );

    int getStratum();
}
