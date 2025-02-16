package com.pinecone.hydra.storage.file.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.Cluster;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.RemoteCluster;

import java.util.List;

public interface RemoteClusterManipulator extends Pinenut {
    RemoteCluster getRemoteCluster(GUID guid, ElementNode element);
    void insert( RemoteCluster remoteCluster );
    void remove( GUID guid );
    void removeClustersByFile( GUID fileGuid );
    RemoteCluster fetchRemoteClustersByFileGuid(GUID guid);
    List<RemoteCluster> fetchRemoteClusterByFileGuid( GUID guid );

    List<RemoteCluster > fetchRemoteClusterByFileGuid( GUID guid, long offset, int pageSize );

    long countRemoteClustersByFileGuid( GUID guid );

    RemoteCluster getLastCluster(GUID guid );
    void removeClusterByFileWithId(GUID fileGuid, long segId );

    long countFileClusters( GUID fileGuid );

    RemoteCluster getClusterByFileWithId( GUID fileGuid, long segId );
}
