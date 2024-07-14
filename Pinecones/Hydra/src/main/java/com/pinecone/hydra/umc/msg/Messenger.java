package com.pinecone.hydra.umc.msg;

import java.io.IOException;

public interface Messenger extends MessageNode {
    UMCMessage sendSyncMsg( UMCMessage request, boolean bNoneBuffered, long nWaitTime ) throws IOException;
}
