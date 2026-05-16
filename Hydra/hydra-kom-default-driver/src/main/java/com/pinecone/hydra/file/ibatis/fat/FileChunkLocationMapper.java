package com.pinecone.hydra.file.ibatis.fat;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.entity.GenericFileChunkLocation;
import com.pinecone.hydra.storage.file.fat.source.FileChunkLocationManipulator;
import com.pinecone.slime.jelly.source.ibatis.IbatisDataAccessObject;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@IbatisDataAccessObject
public interface FileChunkLocationMapper extends FileChunkLocationManipulator {
    void insert( FileChunkLocation location );

    void update( FileChunkLocation location );

    void remove( GUID guid );

    void removeByChunkGuid( GUID chunkGuid );

    void removeByFileGuid( GUID fileGuid );

    GenericFileChunkLocation get( GUID guid );

    GenericFileChunkLocation getReadyLocation( GUID chunkGuid );

    List<GenericFileChunkLocation> listByChunkGuid( GUID chunkGuid );

    Long getMaxEndOffsetByVolumeGuid( GUID volumeGuid );
}
