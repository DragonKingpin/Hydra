package com.pinecone.hydra.umc.msg;

public interface RegisterChannelPool extends ChannelPool {
    long getMajorWaitTimeout();

    RegisterChannelPool setMajorWaitTimeout( long nMillisTimeout );

    int getMaximumPoolSize();
}
