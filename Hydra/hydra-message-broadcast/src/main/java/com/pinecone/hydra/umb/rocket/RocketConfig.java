package com.pinecone.hydra.umb.rocket;

import com.pinecone.hydra.umc.msg.MsgNodeConfig;

public interface RocketConfig extends MsgNodeConfig {
    String getNameServerAddr();

    String getGroupName();

    int getMaxMessageSize();

    int getSendMsgTimeout();

    int getRetryTimesWhenSendFailed();

    @Override
    default long getSyncWaitingMillis() {
        return this.getSendMsgTimeout();
    }
}
