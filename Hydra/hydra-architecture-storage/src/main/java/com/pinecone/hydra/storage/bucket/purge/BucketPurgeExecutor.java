package com.pinecone.hydra.storage.bucket.purge;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

public interface BucketPurgeExecutor extends Pinenut {
    BucketPurgeReport purgeBucket( GUID bucketGuid, @Nullable BucketPurgeProgressListener listener );

    BucketPurgeReport formatBucket( GUID bucketGuid, @Nullable BucketPurgeProgressListener listener );

    default BucketPurgeReport purgeBucket( GUID bucketGuid ) {
        return this.purgeBucket( bucketGuid, null );
    }

    default BucketPurgeReport formatBucket( GUID bucketGuid ) {
        return this.formatBucket( bucketGuid, null );
    }
}
