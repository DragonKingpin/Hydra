package com.pinecone.hydra.umc.wolf.client.reconnect;

import java.io.IOException;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;

import io.netty.channel.Channel;

public interface UlfReconnectFeature extends Pinenut {
    String name();

    void afterReconnectSucceeded( ChannelControlBlock block, Channel oldChannel, Channel newChannel ) throws IOException;
}
