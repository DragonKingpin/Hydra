package com.walnut.sparta.ucdn.console.domain.service.cluster;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ClusterFileSyncTransaction extends Pinenut {
    int getClusterNodeCount();

    void setClusterNodeCount( int clusterNodeCount );

    int checkRemainingCount();

    int decreaseRemainingCount();
}
