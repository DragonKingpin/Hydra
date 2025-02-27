package com.pinecone.hydra.umc.msg;

import java.io.IOException;

import com.pinecone.hydra.umc.wolf.client.ClientConnectArguments;

public interface Messenger extends MessageNode {
    UMCMessage sendSyncMsg( UMCMessage request, boolean bNoneBuffered, long nWaitTime ) throws IOException;

    ClientConnectArguments getConnectionArguments();
}
