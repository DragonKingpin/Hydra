package com.pinecone.hydra.umc.msg.event;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;

public interface ChannelInactiveHandler extends ChannelEventHandler {
    boolean afterChannelInactive( ChannelControlBlock ccb ) throws ChannelHandleException;

    @Override
    default void afterEventTriggered( ChannelControlBlock block ) {

    }
}
