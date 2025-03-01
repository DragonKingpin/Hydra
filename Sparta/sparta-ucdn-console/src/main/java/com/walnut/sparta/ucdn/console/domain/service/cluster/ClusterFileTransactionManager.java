package com.walnut.sparta.ucdn.console.domain.service.cluster;

import com.pinecone.framework.system.regime.arch.Manager;
import com.pinecone.framework.util.id.GUID;

import java.util.concurrent.ConcurrentMap;

public interface ClusterFileTransactionManager extends Manager {

    void register( GUID fileGuid, ConcurrentMap<GUID, ClusterFileSyncTransaction> transactions );

    ConcurrentMap<GUID, ClusterFileSyncTransaction> getTransactions(GUID fileGuid );

    void removeTransactions( GUID fileGuid );

    boolean checkTransactionFinished( GUID fileGuid );


}
