package com.pinecone.hydra.umc.wolf.client;

import com.pinecone.hydra.umc.msg.AsyncMessengerChannelControlBlock;
import com.pinecone.hydra.umc.wolf.NettyChannelControlBlock;
import com.pinecone.hydra.umc.wolf.UlfChannel;

public interface UlfAsyncMessengerChannelControlBlock extends AsyncMessengerChannelControlBlock, NettyChannelControlBlock {
    @Override
    UlfChannel getChannel();
}
