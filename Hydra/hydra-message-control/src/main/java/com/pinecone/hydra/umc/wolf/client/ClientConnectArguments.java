package com.pinecone.hydra.umc.wolf.client;

import com.pinecone.hydra.umc.wolf.MCConnectionArguments;

public interface ClientConnectArguments extends MCConnectionArguments {
    int getParallelChannels();

    void setParallelChannels( int parallelChannels );

    boolean isAutoReconnect();

    void setAutoReconnect( boolean autoReconnect );
}
