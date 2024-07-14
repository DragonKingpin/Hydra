package com.pinecone.hydra.umc.msg;

import com.pinecone.hydra.umc.io.IOLoadBalanceStrategy;

public class IdleFirstBalanceStrategy implements IOLoadBalanceStrategy {
    protected ChannelControlBlock mChannelCB;

    public IdleFirstBalanceStrategy() {
        this(null);
    }

    public IdleFirstBalanceStrategy( ChannelControlBlock channel ) {
        this.apply( channel );
    }

    public IdleFirstBalanceStrategy apply( ChannelControlBlock channel ) {
        this.mChannelCB = channel;
        return this;
    }

    public boolean matched() {
        return this.mChannelCB.getChannelStatus().isIdle();
    }

    public boolean readPriorityMatched() {
        return this.matched();
    }

    public boolean writePriorityMatched() {
        return this.matched();
    }

    public IdleFirstBalanceStrategy clone() {
        IdleFirstBalanceStrategy clone;
        try {
            clone = (IdleFirstBalanceStrategy) super.clone();
        }
        catch ( CloneNotSupportedException e ) {
            throw new InternalError(e);
        }

        return clone;
    }


}
