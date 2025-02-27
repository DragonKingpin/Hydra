package com.pinecone.hydra.umc.wolf;

import com.pinecone.hydra.umc.io.IOLoadBalanceStrategy;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;

public interface UlfIOLoadBalanceStrategy extends IOLoadBalanceStrategy {

    boolean match( ChannelControlBlock ccb );

    UlfIOLoadBalanceStrategy clone();

}
