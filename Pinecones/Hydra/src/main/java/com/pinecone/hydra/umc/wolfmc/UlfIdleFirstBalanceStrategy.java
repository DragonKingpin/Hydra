package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.IdleFirstBalanceStrategy;

public class UlfIdleFirstBalanceStrategy extends IdleFirstBalanceStrategy implements UlfIOLoadBalanceStrategy {
    public UlfIdleFirstBalanceStrategy() {
        super();
    }

    @Override
    public boolean match( ChannelControlBlock ccb ) {
        return ccb.getChannelStatus().isIdle();
    }

    @Override
    public boolean matched( Object condition ) {
        return this.match( (ChannelControlBlock) condition );
    }

    @Override
    public UlfIdleFirstBalanceStrategy clone() {
        return (UlfIdleFirstBalanceStrategy)super.clone();
    }
}
