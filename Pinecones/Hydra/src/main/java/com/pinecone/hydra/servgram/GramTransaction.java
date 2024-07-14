package com.pinecone.hydra.servgram;

import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.orchestration.Exertion;
import com.pinecone.hydra.orchestration.GraphStratum;
import com.pinecone.hydra.orchestration.IntegrityLevel;
import com.pinecone.hydra.orchestration.Transaction;

import java.util.List;

public interface GramTransaction extends Transaction, GraphStratum {
    String  ConfigTransactionsListKey  = "Transactions"  ;
    String  ConfigTransactionNameKey   = "Name"          ;
    String  ConfigPrimaryNameKey       = "Primary"       ;

    GramTransaction loadActionsFromConfig();

    List getTransactionList();

    class TransactionArgs {
        String         name   ;
        ActionType     type     ;
        IntegrityLevel level    ;

        TransactionArgs( String name, ActionType type, IntegrityLevel level ) {
            this.name   = name;
            this.type   = type;
            this.level  = level;
        }
    }
}
