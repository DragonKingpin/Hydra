package com.pinecone.hydra.storage.file.delete;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface UofsMetadataDeleteOperator extends Pinenut {
    void removeFile( GUID guid );

    void removeFolder( GUID guid );

    void removeInternalSymbolic( GUID guid );

    void removeExternalSymbolic( GUID guid );

    void unlink( GUID parentGuid, GUID childGuid );
}
