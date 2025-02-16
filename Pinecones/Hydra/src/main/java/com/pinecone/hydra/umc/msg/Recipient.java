package com.pinecone.hydra.umc.msg;

import com.pinecone.hydra.umc.wolfmc.server.ServerConnectArguments;

public interface Recipient extends MessageNode {

    int getMaximumConnections();

    ServerConnectArguments getConnectionArguments();

}
