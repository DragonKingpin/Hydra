package com.pinecone.hydra.storage.bucket;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.source.BucketManipulator;

import java.util.List;

public class TitanBucketInstrument implements BucketInstrument {
    protected final BucketManipulator bucketManipulator;

    public TitanBucketInstrument( BucketManipulator bucketManipulator ) {
        if ( bucketManipulator == null ) {
            throw new IllegalArgumentException( "bucketManipulator should not be null." );
        }
        this.bucketManipulator = bucketManipulator;
    }

    @Override
    public void insert( Bucket bucket ) {
        this.bucketManipulator.insert( bucket );
    }

    @Override
    public void update( Bucket bucket ) {
        this.bucketManipulator.update( bucket );
    }

    @Override
    public void updateVolume( GUID guid, GUID volumeGuid ) {
        this.bucketManipulator.updateVolume( guid, volumeGuid );
    }

    @Override
    public void updateStatus( GUID guid, String status ) {
        this.bucketManipulator.updateStatus( guid, status );
    }

    @Override
    public void remove( GUID guid ) {
        this.bucketManipulator.remove( guid );
    }

    @Override
    public Bucket get( GUID guid ) {
        return this.bucketManipulator.get( guid );
    }

    @Override
    public Bucket getByBucketIdentifier( String bucketIdentifier ) {
        return this.bucketManipulator.getByBucketIdentifier( bucketIdentifier );
    }

    @Override
    public Bucket getByUserIdentifierAndBucket( String userIdentifier, String bucketName ) {
        return this.bucketManipulator.getByUserIdentifierAndBucket( userIdentifier, bucketName );
    }

    @Override
    public List<GenericBucket> listAll() {
        return this.bucketManipulator.listAll();
    }

    @Override
    public long count( String userIdentifier, String bucketName ) {
        return this.bucketManipulator.count( userIdentifier, bucketName, null );
    }

    @Override
    public long count( String userIdentifier, String bucketName, String bucketIdentifier ) {
        return this.bucketManipulator.count( userIdentifier, bucketName, bucketIdentifier );
    }

    @Override
    public long countByVolumeGuid( GUID volumeGuid ) {
        return this.bucketManipulator.countByVolumeGuid( volumeGuid );
    }

    @Override
    public List<GenericBucket> listPage( String userIdentifier, String bucketName, int offset, int limit ) {
        return this.bucketManipulator.listPage( userIdentifier, bucketName, null, offset, limit );
    }

    @Override
    public List<GenericBucket> listPage( String userIdentifier, String bucketName, String bucketIdentifier, int offset, int limit ) {
        return this.bucketManipulator.listPage( userIdentifier, bucketName, bucketIdentifier, offset, limit );
    }

    @Override
    public boolean existsNode( GUID bucketGuid ) {
        return this.bucketManipulator.existsNode( bucketGuid );
    }

    @Override
    public boolean existsChunk( GUID bucketGuid ) {
        return this.bucketManipulator.existsChunk( bucketGuid );
    }
}
