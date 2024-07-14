package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.wolfmc.client.WolfMCClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

import java.io.IOException;
import java.net.SocketAddress;

public class UlfChannel extends ArchUMCChannel {
    protected EventLoopGroup             mExecutorGroup      ;
    protected Bootstrap                  mBootstrap          ;


    public UlfChannel( MessageNode node ) {
        super( node );

        if( node instanceof WolfMCClient) {
            WolfMCClient messenger   = (WolfMCClient) node;
            this.mExecutorGroup      = messenger.getEventLoopGroup();
            this.mBootstrap          = messenger.getBootstrap();
        }
    }

    public UlfChannel( MessageNode node, Channel nativeChannel, SocketAddress address ) {
        super( node, nativeChannel, address );
    }

    public UlfChannel( MessageNode node, Channel nativeChannel ) {
        this( node, nativeChannel, null );
    }

    public EventLoopGroup    getExecutorGroup() {
        return this.mExecutorGroup;
    }

    public Bootstrap         getBootstrap() {
        return this.mBootstrap;
    }


    @Override
    public void              reconnect() throws IOException {
        if( this.isShutdown() ) {
            this.toConnect( this.getAddress() );
        }
    }

    public ArchUMCChannel    toConnect( SocketAddress address ){
        this.mAddress           = address;
        this.mLastChannelFuture = this.getBootstrap().connect( address );
        this.mChannel           = this.getLastChannelFuture().channel();
        this.mChannelID         = this.mChannel.id();
        return this;
    }

    @Override
    public void release() {
        super.release();

        this.mExecutorGroup      = null;
        this.mBootstrap          = null;
    }
}
