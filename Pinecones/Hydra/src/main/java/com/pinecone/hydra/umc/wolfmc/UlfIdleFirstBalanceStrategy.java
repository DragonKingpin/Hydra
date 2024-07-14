package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.IdleFirstBalanceStrategy;

public class UlfIdleFirstBalanceStrategy extends IdleFirstBalanceStrategy implements UlfIOLoadBalanceStrategy {
    public UlfIdleFirstBalanceStrategy() {
        this(null);
    }

    public UlfIdleFirstBalanceStrategy( ChannelControlBlock channel ) {
        this.apply( channel );
    }

    public UlfIdleFirstBalanceStrategy apply( ChannelControlBlock channel ) {
        this.mChannelCB = channel;
        return this;
    }

    public UlfIdleFirstBalanceStrategy clone() {
        return (UlfIdleFirstBalanceStrategy)super.clone();
    }
}
