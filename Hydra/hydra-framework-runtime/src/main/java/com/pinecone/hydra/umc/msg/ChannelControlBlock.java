package com.pinecone.hydra.umc.msg;

import com.pinecone.hydra.umc.io.IOCounter;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.wolfmc.UlfAsyncMsgHandleAdapter;

import java.io.IOException;

public interface ChannelControlBlock extends Pinenut {
    UMCChannel                 getChannel();

    IOCounter                  getIOCounter();

    boolean                    getInSyncMode();

    UMCTransmit                getTransmit();

    UMCReceiver                getReceiver();

    default MessageNode        getParentMessageNode(){
        return this.getChannel().getParentMessageNode();
    }

    void                       sendMsg( UMCMessage message, boolean bNoneBuffered ) throws IOException;

    void                       release();

    void                       close();

    default boolean            isShutdown(){
        return this.getChannel().isShutdown();
    }

    ChannelStatus              getChannelStatus();


    void                       pushMsgHandle ( UlfAsyncMsgHandleAdapter msgHandle );

    UlfAsyncMsgHandleAdapter   pollMsgHandle ( long nWaitMillis ) throws InterruptedException;
}
