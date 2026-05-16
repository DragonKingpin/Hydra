package com.pinecone.hydra.storage.bucket;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;

import java.util.List;

public interface BucketInstrument extends Pinenut {
    void insert( Bucket bucket );

    void update( Bucket bucket );

    void updateVolume( GUID guid, GUID volumeGuid );

    void remove( GUID guid );

    Bucket get( GUID guid );

    Bucket getByUserIdentifierAndBucket( String userIdentifier, String bucketName );

    List<GenericBucket> listAll();

    long count( String userIdentifier, String bucketName );

    List<GenericBucket> listPage( String userIdentifier, String bucketName, int offset, int limit );

    boolean existsNode( GUID bucketGuid );

    boolean existsChunk( GUID bucketGuid );

    default boolean isEmpty( GUID bucketGuid ) {
        return !this.existsNode( bucketGuid ) && !this.existsChunk( bucketGuid );
    }

    default void bindVolume( GUID bucketGuid, GUID volumeGuid ) {
        if ( !this.isEmpty( bucketGuid ) ) {
            throw new IllegalStateException( "Bucket is not empty, format it before rebinding volume: " + bucketGuid );
        }
        this.updateVolume( bucketGuid, volumeGuid );
    }
}
