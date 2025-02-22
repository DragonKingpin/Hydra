package com.pinecone.hydra.umc.msg.event;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;

public interface ChannelEventHandler extends Pinenut {
    void afterEventTriggered( ChannelControlBlock block );
}
