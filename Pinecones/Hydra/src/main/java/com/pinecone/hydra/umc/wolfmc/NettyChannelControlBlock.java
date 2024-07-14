package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;

public interface NettyChannelControlBlock extends ChannelControlBlock {
    @Override
    NettyUMCChannel     getChannel();
}
