package com.pinecone.hydra.storage.file.source;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.hydra.storage.file.entity.ElementNode;
import com.pinecone.hydra.storage.file.entity.LocalCluster;

import java.util.List;

public interface LocalClusterManipulator extends Pinenut {
    LocalCluster getLocalCluster(GUID guid, ElementNode element);
    void insert( LocalCluster localCluster );
    void remove( GUID guid );
    void removeClustersByFile( GUID fileGuid );
    LocalCluster getLocalClusterByGuid(GUID guid);
    List<LocalCluster> getLocalClusterByFileGuid(GUID guid );
    LocalCluster getClusterByFileWithId(GUID fileGuid, long segId );
    void update( LocalCluster localCluster );
    void removeClusterByFileWithId( GUID fileGuid, long segId );
}
