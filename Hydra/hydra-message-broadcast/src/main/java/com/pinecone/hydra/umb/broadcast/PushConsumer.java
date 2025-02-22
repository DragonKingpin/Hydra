package com.pinecone.hydra.umb.broadcast;

import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.UlfPackageMessageHandler;

public interface PushConsumer extends BroadcastConsumer {
    void start( UlfPackageMessageHandler handler ) throws UMBServiceException ;
}
