package com.pinecone.hydra.storage.bucket.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.Bucket;
import com.pinecone.hydra.storage.bucket.GenericBucket;

import java.util.List;

public interface BucketManipulator extends Pinenut {
    void insert( Bucket bucket );

    void update( Bucket bucket );

    void updateVolume( GUID guid, GUID volumeGuid );

    void updateStatus( GUID guid, String status );

    void remove( GUID guid );

    GenericBucket get( GUID guid );

    GenericBucket getByBucketIdentifier( String bucketIdentifier );

    GenericBucket getByUserIdentifierAndBucket( String userIdentifier, String bucketName );

    List<GenericBucket> listAll();

    default long count( String userIdentifier, String bucketName ) {
        return this.count( userIdentifier, bucketName, null );
    }

    long count( String userIdentifier, String bucketName, String bucketIdentifier );

    long countByVolumeGuid( GUID volumeGuid );

    List<GenericBucket> listByVolumeGuid( GUID volumeGuid, int offset, int limit );

    default List<GenericBucket> listPage( String userIdentifier, String bucketName, int offset, int limit ) {
        return this.listPage( userIdentifier, bucketName, null, offset, limit );
    }

    List<GenericBucket> listPage( String userIdentifier, String bucketName, String bucketIdentifier, int offset, int limit );

    boolean existsNode( GUID bucketGuid );

    boolean existsChunk( GUID bucketGuid );
}
