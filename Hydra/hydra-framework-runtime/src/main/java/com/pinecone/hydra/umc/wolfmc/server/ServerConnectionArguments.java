package com.pinecone.hydra.umc.wolfmc.server;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.umc.wolfmc.SharedConnectionArguments;
import com.pinecone.hydra.umc.wolfmc.client.ArchAsyncMessenger;

public class ServerConnectionArguments extends SharedConnectionArguments implements ServerConnectArguments {
    protected int mnMaximumClients; // 0 <= for unlimited clients

    public ServerConnectionArguments( JSONObject args ) {
        super( args );
        this.mnMaximumClients  = args.optInt( "MaximumClients", 0 );
    }

    public ServerConnectionArguments( ArchAsyncMessenger args ) {
        this( args.getSectionConf() );
    }

    @Override
    public int getMaximumClients() {
        return this.mnMaximumClients;
    }

    @Override
    public void setMaximumClients( int mnMaximumClients ) {
        this.mnMaximumClients = mnMaximumClients;
    }
}
