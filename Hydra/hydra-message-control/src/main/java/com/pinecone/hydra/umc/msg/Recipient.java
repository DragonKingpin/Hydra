package com.pinecone.hydra.umc.msg;

import com.pinecone.hydra.umc.wolf.server.ServerConnectArguments;

public interface Recipient extends MessageNode {

    int getMaximumConnections();

    ServerConnectArguments getConnectionArguments();

}
