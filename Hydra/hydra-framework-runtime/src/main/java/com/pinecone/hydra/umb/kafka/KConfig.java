package com.pinecone.hydra.umb.kafka;

import com.pinecone.hydra.umc.msg.MsgNodeConfig;

public interface KConfig extends MsgNodeConfig {
    String getServer();

    String getAutoOffsetReset();

    long getDefaultPollHandleMillis();

    @Override
    default long getSyncWaitingMillis() {
        return this.getDefaultPollHandleMillis();
    }
}
