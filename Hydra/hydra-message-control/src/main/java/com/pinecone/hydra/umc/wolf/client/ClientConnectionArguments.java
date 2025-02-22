package com.pinecone.hydra.umc.wolf.client;

import com.pinecone.framework.util.json.JSONObject;
import com.pinecone.hydra.umc.wolf.SharedConnectionArguments;

public class ClientConnectionArguments extends SharedConnectionArguments implements ClientConnectArguments {
    protected int            mnParallelChannels;

    protected boolean        mbAutoReconnect;

    public ClientConnectionArguments( JSONObject args ) {
        super( args );
        this.mnParallelChannels  = args.optInt( "ParallelChannels", 1 );
        this.mbAutoReconnect     = args.optBoolean( "AutoReconnect", false );
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

    @Override
    public boolean isAutoReconnect() {
        return this.mbAutoReconnect;
    }

    @Override
    public void setAutoReconnect( boolean autoReconnect ) {
        this.mbAutoReconnect = autoReconnect;
    }
}
