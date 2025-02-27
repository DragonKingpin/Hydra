package com.pinecone.hydra.umct.decipher;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umct.UMCConnection;
import com.pinecone.hydra.umct.husky.HuskyServiceErrorMessages;

public interface HeaderDecipher extends Pinenut {
    Object eval( Object that, @Nullable Object descriptor, String key );

    default String evalString( Object that, @Nullable Object descriptor, String key ) {
        return this.eval( that, descriptor, key ).toString();
    }

    default Number evalNumber( Object that, @Nullable Object descriptor, String key ) {
        Object o = this.eval( that, descriptor, key );
        if( o instanceof Number ) {
            return (Number) o;
        }
        return null;
    }

    Collection<Object > values( Object that, @Nullable Object descriptor, @Nullable Object argTpl );

    Object[] evals( Object that, @Nullable Object descriptor, List<String > keys, @Nullable Object argTpl );

    String getServicePath( Object that );

    default void sendIllegalMessage( UMCConnection connection ) throws IOException {
        connection.getTransmit().sendMsg( HuskyServiceErrorMessages.HCTP_ILLEGAL_MESSAGE );
    }

    default void sendInternalError( UMCConnection connection ) throws IOException {
        connection.getTransmit().sendMsg( HuskyServiceErrorMessages.HCTP_INTERNAL_ERROR );
    }

    UMCMessage assembleReturnMsg( Object that, Object descriptor ) ;
}
