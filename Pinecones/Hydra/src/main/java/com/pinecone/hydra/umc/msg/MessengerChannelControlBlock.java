package com.pinecone.hydra.umc.msg;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

public interface MessengerChannelControlBlock extends ChannelControlBlock {
    @Override
    default Messenger getParentMessageNode(){
        return (Messenger) this.getChannel().getParentMessageNode();
    }

    Lock              getSynRequestLock();

    UMCMessage        sendSyncMsg( UMCMessage message, boolean bNoneBuffered, long nWaitTime ) throws IOException;
}
