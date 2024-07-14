package com.pinecone.hydra.umc.wolfmc.client;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.umc.wolfmc.SharedConnectionArguments;

public class ClientConnectionArguments extends SharedConnectionArguments implements ClientConnectArguments {
    protected int            mnParallelChannels;

    public ClientConnectionArguments( JSONObject args ) {
        super( args );
        this.mnParallelChannels  = args.optInt( "ParallelChannels", 1 );
    }

    public ClientConnectionArguments( ArchAsyncMessenger args ) {
        this( args.getSectionConf() );
    }

    @Override
    public int getParallelChannels() {
        return this.mnParallelChannels;
    }

    @Override
    public void setParallelChannels( int parallelChannels ) {
        this.mnParallelChannels = parallelChannels;
    }
}
