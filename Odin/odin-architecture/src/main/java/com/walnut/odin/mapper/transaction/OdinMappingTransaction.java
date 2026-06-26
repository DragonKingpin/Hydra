package com.walnut.odin.mapper.transaction;

import java.util.List;

import com.pinecone.framework.system.prototype.Pinenut;

public interface OdinMappingTransaction extends Pinenut {
    <T> T required( OdinMappingTransactionCallback<T> callback );

    default void required( List<? extends OdinMappingTransactionAction> actions ) {
        this.required( scope -> {
            if ( actions != null ) {
                for ( OdinMappingTransactionAction action : actions ) {
                    if ( action != null ) {
                        action.execute( scope );
                    }
                }
            }
            return null;
        } );
    }
}
