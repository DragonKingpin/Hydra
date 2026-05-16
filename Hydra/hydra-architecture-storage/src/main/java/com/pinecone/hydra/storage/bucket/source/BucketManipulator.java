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

    void remove( GUID guid );

    GenericBucket get( GUID guid );

    GenericBucket getByUserIdentifierAndBucket( String userIdentifier, String bucketName );

    List<GenericBucket> listAll();

    long count( String userIdentifier, String bucketName );

    List<GenericBucket> listPage( String userIdentifier, String bucketName, int offset, int limit );

    boolean existsNode( GUID bucketGuid );

    boolean existsChunk( GUID bucketGuid );
}
