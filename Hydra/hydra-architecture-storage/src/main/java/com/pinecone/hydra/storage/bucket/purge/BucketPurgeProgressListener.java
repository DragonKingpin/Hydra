package com.pinecone.hydra.storage.bucket.purge;

import com.pinecone.framework.system.prototype.Pinenut;

public interface BucketPurgeProgressListener extends Pinenut {
    void onProgress( BucketPurgeProgress progress );
}
