package com.pinecone.hydra.umc.wolfmc.server;

import com.pinecone.hydra.umc.msg.RecipientChannelControlBlock;
import com.pinecone.hydra.umc.wolfmc.NettyChannelControlBlock;
import com.pinecone.hydra.umc.wolfmc.UlfChannel;

public interface UlfRecipientChannelControlBlock extends RecipientChannelControlBlock, NettyChannelControlBlock {
    @Override
    UlfChannel getChannel();
}
