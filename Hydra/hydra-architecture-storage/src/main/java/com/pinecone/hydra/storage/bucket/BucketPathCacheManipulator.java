package com.pinecone.hydra.storage.bucket;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface BucketPathCacheManipulator extends Pinenut {
    GUID queryGUIDByBucketPath( GUID bucketGuid, String path );

    long countPathCacheByBucketGuid( GUID bucketGuid );

    void deletePathCacheByBucketGuid( GUID bucketGuid );
}
