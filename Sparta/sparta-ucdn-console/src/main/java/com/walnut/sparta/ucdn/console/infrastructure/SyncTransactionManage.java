package com.walnut.sparta.ucdn.console.infrastructure;

import com.pinecone.framework.util.id.GUID;

import javax.websocket.Session;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SyncTransactionManage implements TransactionManage{
    private ConcurrentMap< GUID, ConcurrentMap<GUID, SyncTransaction>> transactionMap;

    public SyncTransactionManage(){
        this.transactionMap = new ConcurrentHashMap<>();
    }

    @Override
    public void register(GUID fileGuid, ConcurrentMap<GUID, SyncTransaction> transactions) {
        this.transactionMap.put( fileGuid, transactions );
    }

    @Override
    public ConcurrentMap<GUID, SyncTransaction> getTransactions(GUID fileGuid) {
        return this.transactionMap.get( fileGuid );
    }

    @Override
    public void removeTransactions(GUID fileGuid) {
        this.transactionMap.remove( fileGuid );
    }

    @Override
    public boolean checkTransactionOver(GUID fileGuid) {
        ConcurrentMap<GUID, SyncTransaction> transactions = this.getTransactions(fileGuid);
        for( SyncTransaction syncTransaction : transactions.values() ){
            if( syncTransaction.checkRemainingNum() != 0 ){
                return false;
            }
        }
        return true;
    }

}
