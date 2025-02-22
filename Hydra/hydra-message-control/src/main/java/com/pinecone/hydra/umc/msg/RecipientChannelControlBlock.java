package com.pinecone.hydra.umc.msg;

public interface RecipientChannelControlBlock extends ChannelControlBlock {
    @Override
    default Recipient getParentMessageNode(){
        return (Recipient) this.getChannel().getParentMessageNode();
    }
}
