package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.io.IOLoadBalanceStrategy;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;

public interface UlfIOLoadBalanceStrategy extends IOLoadBalanceStrategy {
    UlfIOLoadBalanceStrategy apply( ChannelControlBlock channel ) ;

    UlfIOLoadBalanceStrategy clone();
}
