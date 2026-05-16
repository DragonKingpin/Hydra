package com.pinecone.hydra.storage.bucket;

import com.pinecone.framework.system.prototype.Pinenut;

public class BucketResolver implements Pinenut {
    protected final BucketInstrument mBucketInstrument;

    public BucketResolver( BucketInstrument bucketInstrument ) {
        this.mBucketInstrument = bucketInstrument;
    }

    public Bucket resolve( String userIdentifier, String bucketName ) {
        Bucket bucket = this.mBucketInstrument.getByUserIdentifierAndBucket( userIdentifier, bucketName );
        if ( bucket == null ) {
            throw new IllegalArgumentException( "UOFS bucket not found: " + userIdentifier + "@" + bucketName );
        }
        if ( bucket.getVolumeGuid() == null ) {
            throw new IllegalStateException( "UOFS bucket has no bound volume: " + userIdentifier + "@" + bucketName );
        }
        return bucket;
    }
}
