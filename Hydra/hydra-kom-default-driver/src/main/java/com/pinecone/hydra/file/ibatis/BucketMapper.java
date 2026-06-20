package com.pinecone.hydra.file.ibatis;

import java.util.List;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.bucket.Bucket;
import com.pinecone.hydra.storage.bucket.GenericBucket;
import com.pinecone.hydra.storage.bucket.source.BucketManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@IbatisDataAccessObject
public interface BucketMapper extends BucketManipulator {
    @Override
    void insert( Bucket bucket );

    @Override
    void update( Bucket bucket );

    @Override
    void updateVolume( @Param( "guid" ) GUID guid, @Param( "volumeGuid" ) GUID volumeGuid );

    @Override
    void updateStatus( @Param( "guid" ) GUID guid, @Param( "status" ) String status );

    @Override
    void remove( GUID guid );

    @Override
    GenericBucket get( GUID guid );

    @Override
    GenericBucket getByBucketIdentifier( @Param( "bucketIdentifier" ) String bucketIdentifier );

    @Override
    GenericBucket getByUserIdentifierAndBucket( @Param( "userIdentifier" ) String userIdentifier, @Param( "bucketName" ) String bucketName );

    @Override
    List<GenericBucket> listAll();

    @Override
    long count(
            @Param( "userIdentifier" ) String userIdentifier,
            @Param( "bucketName" ) String bucketName,
            @Param( "bucketIdentifier" ) String bucketIdentifier
    );

    @Override
    long countByVolumeGuid( @Param( "volumeGuid" ) GUID volumeGuid );

    @Override
    List<GenericBucket> listPage(
            @Param( "userIdentifier" ) String userIdentifier,
            @Param( "bucketName" ) String bucketName,
            @Param( "bucketIdentifier" ) String bucketIdentifier,
            @Param( "offset" ) int offset,
            @Param( "limit" ) int limit
    );

    @Override
    boolean existsNode( GUID bucketGuid );

    @Override
    boolean existsChunk( GUID bucketGuid );
}
