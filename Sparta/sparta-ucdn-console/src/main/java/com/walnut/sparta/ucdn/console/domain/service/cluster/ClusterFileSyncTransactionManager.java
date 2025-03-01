package com.walnut.sparta.ucdn.console.domain.service.cluster;

import com.pinecone.framework.util.id.GUID;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ClusterFileSyncTransactionManager implements ClusterFileTransactionManager {
    private ConcurrentMap< GUID, ConcurrentMap<GUID, ClusterFileSyncTransaction>> transactionMap;

    public ClusterFileSyncTransactionManager(){
        this.transactionMap = new ConcurrentHashMap<>();
    }

    @Override
    public void register(GUID fileGuid, ConcurrentMap<GUID, ClusterFileSyncTransaction> transactions) {
        this.transactionMap.put( fileGuid, transactions );
    }

    @Override
    public ConcurrentMap<GUID, ClusterFileSyncTransaction> getTransactions(GUID fileGuid ) {
        return this.transactionMap.get( fileGuid );
    }

    @Override
    public void removeTransactions( GUID fileGuid ) {
        this.transactionMap.remove( fileGuid );
    }

    @Override
    public boolean checkTransactionFinished( GUID fileGuid ) {
        ConcurrentMap<GUID, ClusterFileSyncTransaction> transactions = this.getTransactions(fileGuid);
        for( ClusterFileSyncTransaction clusterFileSyncTransaction : transactions.values() ){
            if( clusterFileSyncTransaction.checkRemainingCount() != 0 ){
                return false;
            }
        }
        return true;
    }

}
