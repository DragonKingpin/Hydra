package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.ChannelPool;
import com.pinecone.hydra.umc.msg.CascadeMessageNode;
import com.pinecone.hydra.umc.msg.event.ChannelDataInterceptor;
import com.pinecone.hydra.umc.msg.event.ChannelInactiveHandler;

public interface UlfMessageNode extends CascadeMessageNode {
    ChannelPool          getChannelPool();

    void                 close();

    UlfMessageNode       registerChannelInactiveHandler( ChannelInactiveHandler handler ) throws IllegalStateException;

    UlfMessageNode       deregisterChannelInactiveHandler( ChannelInactiveHandler handler ) throws IllegalStateException;

    UlfMessageNode       registerArrivedDataInterceptor( ChannelDataInterceptor handler ) throws IllegalStateException;

    UlfMessageNode       deregisterArrivedDataInterceptor( ChannelDataInterceptor handler ) throws IllegalStateException;
}
