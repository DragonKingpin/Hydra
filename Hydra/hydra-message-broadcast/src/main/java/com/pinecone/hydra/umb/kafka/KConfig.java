package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umc.msg.MsgNodeConfig;

public interface KConfig extends MsgNodeConfig {
    String getMszServer();

    String getMszAutoOffsetReset();

    long getMnDefaultPollHandleMillis();

    @Override
    default long getSyncWaitingMillis() {
        return this.getMnDefaultPollHandleMillis();
    }
}
