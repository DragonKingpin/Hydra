package com.pinecone.hydra.storage.file.fat.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.fat.entity.FileChunkLocation;
import com.pinecone.hydra.storage.file.fat.entity.GenericFileChunkLocation;

import java.util.List;

public interface FileChunkLocationManipulator extends Pinenut {
    void insert( FileChunkLocation location );

    void update( FileChunkLocation location );

    void remove( GUID guid );

    void removeByChunkGuid( GUID chunkGuid );

    void removeByFileGuid( GUID fileGuid );

    FileChunkLocation get( GUID guid );

    FileChunkLocation getReadyLocation( GUID chunkGuid );

    List<GenericFileChunkLocation> listByChunkGuid( GUID chunkGuid );

    long countAll();

    long countByBucketGuid( GUID bucketGuid );

    long countByVolumeGuid( GUID volumeGuid );

    List<GenericFileChunkLocation> listPage( int offset, int limit );

    Long getMaxEndOffsetByVolumeGuid( GUID volumeGuid );

    Long sumLengthByBucketGuid( GUID bucketGuid );

    void deleteByBucketGuid( GUID bucketGuid );
}
