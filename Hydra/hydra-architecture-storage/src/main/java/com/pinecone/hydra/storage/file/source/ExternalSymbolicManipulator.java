package com.pinecone.hydra.storage.file.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ExternalSymbolic;

import java.util.List;

public interface ExternalSymbolicManipulator extends Pinenut {
    void insert( ExternalSymbolic externalSymbolic );
    void remove( GUID guid );
    void rename( GUID guid, String newName );
    ExternalSymbolic getSymbolicByGuid( GUID guid );

    ExternalSymbolic getSymbolicByNameGuid( String nodeName, GUID nodeGUID );

    boolean isSymbolicMatchedByNameGuid( String nodeName, GUID nodeGUID );

    List<? extends ExternalSymbolic> listPage( String keyword, int offset, int limit );

    long count( String keyword );

    long countByBucketGuid( GUID bucketGuid );

    void deleteByBucketGuid( GUID bucketGuid );
}
