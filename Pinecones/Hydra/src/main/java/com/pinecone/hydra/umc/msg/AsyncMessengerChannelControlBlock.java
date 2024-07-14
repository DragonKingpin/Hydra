package com.pinecone.hydra.umc.msg;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;

public interface AsyncMessengerChannelControlBlock extends MessengerChannelControlBlock {
    @Override
    default AsyncMessenger     getParentMessageNode(){
        return (AsyncMessenger) this.getChannel().getParentMessageNode();
    }

    void                       sendAsynMsg( UMCMessage message, boolean bNoneBuffered ) throws IOException;

    @Override
    default void               sendMsg( UMCMessage message, boolean bNoneBuffered ) throws IOException {
        this.sendAsynMsg( message, bNoneBuffered );
    }
}
