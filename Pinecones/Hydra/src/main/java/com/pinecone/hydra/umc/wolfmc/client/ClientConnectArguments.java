package com.pinecone.hydra.umc.wolfmc.client;

import com.pinecone.hydra.umc.wolfmc.MCConnectionArguments;

public interface ClientConnectArguments extends MCConnectionArguments {
    int getParallelChannels();

    void setParallelChannels( int parallelChannels );
}
