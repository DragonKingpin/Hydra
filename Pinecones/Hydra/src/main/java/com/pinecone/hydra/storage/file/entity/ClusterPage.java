package com.pinecone.hydra.storage.file.entity;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ClusterPage extends Pinenut {
    int getPageSize() ;

    int getPageSum() ;

    long getCurrentPage() ;

    long getClusters() ;

    Cluster getCluster( long segId ) ;

    LocalCluster getLocalCluster( long segId );

}
