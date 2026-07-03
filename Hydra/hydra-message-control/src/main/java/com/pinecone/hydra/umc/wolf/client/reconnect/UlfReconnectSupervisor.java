package com.pinecone.hydra.umc.wolf.client.reconnect;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;

public interface UlfReconnectSupervisor extends Pinenut {
    void submit( ChannelControlBlock block, UlfReconnectFeature feature );

    void clear();

    boolean isReconnecting( ChannelControlBlock block );
}
