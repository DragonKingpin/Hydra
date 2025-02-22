package com.pinecone.hydra.umc.wolf.server;

import com.pinecone.hydra.umc.msg.RecipientChannelControlBlock;
import com.pinecone.hydra.umc.wolf.NettyChannelControlBlock;
import com.pinecone.hydra.umc.wolf.UlfChannel;

public interface UlfRecipientChannelControlBlock extends RecipientChannelControlBlock, NettyChannelControlBlock {
    @Override
    UlfChannel getChannel();
}
