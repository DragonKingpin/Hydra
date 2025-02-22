package com.pinecone.hydra.umc.wolf;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;

public interface NettyChannelControlBlock extends ChannelControlBlock {
    @Override
    NettyUMCChannel     getChannel();
}
