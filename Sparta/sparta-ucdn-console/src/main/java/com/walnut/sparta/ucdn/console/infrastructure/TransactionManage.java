package com.walnut.sparta.ucdn.console.infrastructure;

import com.pinecone.framework.util.id.GUID;

import javax.websocket.Session;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

public interface TransactionManage {
    void register(GUID fileGuid, ConcurrentMap<GUID, SyncTransaction> transactions);

    ConcurrentMap<GUID, SyncTransaction> getTransactions( GUID fileGuid );

    void removeTransactions( GUID fileGuid );

    boolean checkTransactionOver( GUID fileGuid );


}
