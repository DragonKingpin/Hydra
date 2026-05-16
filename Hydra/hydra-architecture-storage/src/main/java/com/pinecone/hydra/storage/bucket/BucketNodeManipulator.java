package com.pinecone.hydra.storage.bucket;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface BucketNodeManipulator extends Pinenut {
    void updateBucketGuid( GUID guid, GUID bucketGuid );
}
