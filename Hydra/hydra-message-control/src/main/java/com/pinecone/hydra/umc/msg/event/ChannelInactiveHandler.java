package com.pinecone.hydra.umc.msg.event;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;

import io.netty.channel.ChannelHandlerContext;

public interface ChannelInactiveHandler extends ChannelEventHandler {
    boolean afterChannelInactive( ChannelControlBlock ccb, Object context ) throws ChannelHandleException;

    @Override
    default void afterEventTriggered( ChannelControlBlock block, Object context ) {

    }
}
