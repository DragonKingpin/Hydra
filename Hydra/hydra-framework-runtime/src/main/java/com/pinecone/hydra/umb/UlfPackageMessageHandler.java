package com.pinecone.hydra.umb;

import com.pinecone.framework.system.prototype.Pinenut;

public interface UlfPackageMessageHandler extends Pinenut {
    default void onSuccessfulMsgReceived ( byte[] body, Object[] args ) throws Exception {

    }

    default void onErrorMsgReceived      ( byte[] body, Object[] args ) throws Exception {

    }

    default void onError                 ( Object data, Throwable cause ) {

    }
}
