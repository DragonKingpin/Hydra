package com.pinecone.hydra.umc.msg;

import com.pinecone.framework.system.prototype.Pinenut;

public interface ChannelStatus extends Pinenut {
    String getName();

    int getValue();

    byte getByteValue();

    boolean isIdle();

    boolean isTerminated();

    boolean isWaitingForIOCompleted();

    boolean isWaitingForLocalCompleted();

    default boolean isWaitingForOperationCompleted() {
        return this.isWaitingForIOCompleted() || this.isWaitingForLocalCompleted();
    }

    boolean isAsynAvailable();

    boolean isSyncAvailable();

    default String toJSONString() {
        return "\"" + this.toString() + "\"";
    }
}
