package com.pinecone.hydra.umc.wolf.server;

import com.pinecone.hydra.umc.wolf.MCConnectionArguments;

public interface ServerConnectArguments extends MCConnectionArguments {
    int getMaximumClients() ;

    void setMaximumClients( int mnMaximumClients );
}
