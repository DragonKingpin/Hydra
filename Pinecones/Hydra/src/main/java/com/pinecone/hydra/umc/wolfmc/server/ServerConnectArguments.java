package com.pinecone.hydra.umc.wolfmc.server;

import com.pinecone.hydra.umc.wolfmc.MCConnectionArguments;

public interface ServerConnectArguments extends MCConnectionArguments {
    int getMaximumClients() ;

    void setMaximumClients( int mnMaximumClients );
}
