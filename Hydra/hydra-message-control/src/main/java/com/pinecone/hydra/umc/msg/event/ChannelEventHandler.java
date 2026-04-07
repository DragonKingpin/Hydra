package com.pinecone.hydra.umc.msg.event;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;

import io.netty.channel.ChannelHandlerContext;

public interface ChannelEventHandler extends Pinenut {
    void afterEventTriggered( ChannelControlBlock block, Object context );
}
