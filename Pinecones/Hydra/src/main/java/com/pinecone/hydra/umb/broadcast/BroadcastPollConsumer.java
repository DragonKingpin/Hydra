package com.pinecone.hydra.umb.broadcast;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umb.UMBServiceException;
import com.pinecone.hydra.umb.UlfPackageMessageHandler;

import java.util.List;

public interface BroadcastPollConsumer extends BroadcastConsumer {

    void close();

    void start( UlfPackageMessageHandler handler ) throws UMBServiceException;

    List<PollResult> startPull(long mils );
}