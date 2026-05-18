package com.pinecone.hydra.storage.file.fat.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.fat.entity.FileChunk;
import com.pinecone.hydra.storage.file.fat.entity.GenericFileChunk;

import java.util.List;

public interface FileChunkManipulator extends Pinenut {
    void insert( FileChunk chunk );

    void update( FileChunk chunk );

    void remove( GUID guid );

    void removeByFileGuid( GUID fileGuid );

    FileChunk get( GUID guid );

    FileChunk getByFileGuidAndIndex( GUID fileGuid, long chunkIndex );

    List<GenericFileChunk> listByFileGuid( GUID fileGuid );

    List<GenericFileChunk> listByFileRange( GUID fileGuid, long startOffset, long endOffset );
}
