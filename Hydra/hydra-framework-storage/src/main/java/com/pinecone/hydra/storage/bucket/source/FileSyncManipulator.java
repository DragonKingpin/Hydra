package com.pinecone.hydra.storage.bucket.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface FileSyncManipulator extends Pinenut {
    void insert(GUID fileGuid, int state, GUID siteGuid);

    void remove( GUID fileGuid );

    void updateState( GUID fileGuid, int state );

    Integer queryState( GUID fileGuid );
}
