package com.pinecone.hydra.file.ibatis.fat;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.GenericFileChunk;
import com.pinecone.hydra.storage.file.fat.source.FileChunkManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface FileChunkMapper extends FileChunkManipulator {
    void insert( FileChunk chunk );

    void update( FileChunk chunk );

    void remove( GUID guid );

    void removeByFileGuid( GUID fileGuid );

    GenericFileChunk get( GUID guid );

    GenericFileChunk getByFileGuidAndIndex( @Param( "fileGuid" ) GUID fileGuid, @Param( "chunkIndex" ) long chunkIndex );

    List<GenericFileChunk> listByFileGuid( GUID fileGuid );

    long countAll();

    @Override
    long countByBucketGuid( @Param("bucketGuid") GUID bucketGuid );

    List<GenericFileChunk> listPage( @Param("offset") int offset, @Param("limit") int limit );

    List<GenericFileChunk> listByFileRange( @Param( "fileGuid" ) GUID fileGuid, @Param( "startOffset" ) long startOffset, @Param( "endOffset" ) long endOffset );

    Long sumValidSizeByBucketGuid( @Param("bucketGuid") GUID bucketGuid );

    @Override
    void deleteByBucketGuid( @Param("bucketGuid") GUID bucketGuid );
}
