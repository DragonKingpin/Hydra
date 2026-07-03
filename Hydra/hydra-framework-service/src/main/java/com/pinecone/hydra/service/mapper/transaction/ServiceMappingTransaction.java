package com.pinecone.hydra.service.mapper.transaction;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ServiceMappingTransaction extends Pinenut {
    <T> T required( ServiceMappingTransactionCallback<T> callback );

    default void required( List<? extends ServiceMappingTransactionAction> actions ) {
        this.required( scope -> {
            if ( actions != null ) {
                for ( ServiceMappingTransactionAction action : actions ) {
                    if ( action != null ) {
                        action.execute( scope );
                    }
                }
            }
            return null;
        } );
    }
}
