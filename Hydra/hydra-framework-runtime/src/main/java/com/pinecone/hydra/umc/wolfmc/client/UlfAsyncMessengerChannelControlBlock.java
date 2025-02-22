package com.pinecone.hydra.umc.wolfmc.client;

import com.pinecone.hydra.umc.msg.AsyncMessengerChannelControlBlock;
import com.pinecone.hydra.umc.wolfmc.NettyChannelControlBlock;
import com.pinecone.hydra.umc.wolfmc.UlfChannel;

public interface UlfAsyncMessengerChannelControlBlock extends AsyncMessengerChannelControlBlock, NettyChannelControlBlock {
    @Override
    UlfChannel getChannel();
}
