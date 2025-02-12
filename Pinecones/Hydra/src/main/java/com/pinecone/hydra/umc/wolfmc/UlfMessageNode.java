package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.ChannelPool;
import com.pinecone.hydra.umc.msg.CascadeMessageNode;

public interface UlfMessageNode extends CascadeMessageNode {
    ChannelPool          getChannelPool();

    void                 close();

    UlfMessageNode       registerChannelInactiveHandler(ChannelInactiveHandler handler ) throws IllegalStateException;

    UlfMessageNode       deregisterChannelInactiveHandler( ChannelInactiveHandler handler ) throws IllegalStateException;
}
