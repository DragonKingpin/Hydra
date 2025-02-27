package com.pinecone.hydra.umc.msg;

import com.pinecone.hydra.umc.io.IOLoadBalanceStrategy;

public class IdleFirstBalanceStrategy implements IOLoadBalanceStrategy {
    public IdleFirstBalanceStrategy() {

    }

    @Override
    public boolean matched( Object condition ) {
        ChannelControlBlock ccb = (ChannelControlBlock) condition;
        return ccb.getChannelStatus().isIdle();
    }

    @Override
    public boolean readPriorityMatched( Object condition ) {
        return this.matched( condition );
    }

    @Override
    public boolean writePriorityMatched( Object condition ) {
        return this.matched( condition );
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
