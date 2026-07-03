package com.pinecone.hydra.umc.wolf;

import com.pinecone.framework.system.Nullable;
import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

    // Auto set address while connection.
    public UlfChannel( MessageNode node, Channel nativeChannel, @Nullable SocketAddress address ) {
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
    public void              reconnect( long mils ) throws IOException {
        if ( this.isShutdown() ) {
            ChannelFuture future = this.openReconnectCandidate( this.getAddress() );

            try {
                if ( mils != -1 ) {
                    if ( !future.await( mils, TimeUnit.MILLISECONDS ) ) {
                        future.channel().close();
                        throw new IOException( new TimeoutException( "Reconnect timed out." ) );
                    }
                }
                else {
                    future.await();
                }
            }
            catch ( InterruptedException e ) {
                Thread.currentThread().interrupt();
                throw new IOException( e );
            }

            if ( !future.isSuccess() ) {
                future.channel().close();
                throw new IOException( future.cause() );
            }

            this.commitReconnectCandidate( future.channel(), future );

            try{
                ( (Slf4jTraceable) this.getParentMessageNode() ).getLogger().info(
                        "[ChannelReconnect] <id:`{}`, Addr: `{}`>", this.getNativeHandle().id(), this.getAddress()
                );
            }
            catch ( ClassCastException ignore ) {
                // Ignore them.
            }
        }
    }

    @Override
    public void              reconnect() throws IOException {
        this.reconnect( -1 );
    }

    public static void copyChannelAttr( Channel leg, Channel neo, String key ) {
        Object val = leg.attr( AttributeKey.valueOf( key ) ).get();
        if ( val != null ) {
            neo.attr( AttributeKey.valueOf( key ) ).set( val );
        }
    }

    public ChannelFuture      openReconnectCandidate( SocketAddress address ) {
        this.mAddress = address;

        ChannelFuture future = this.getBootstrap().connect( address );
        Channel channel      = future.channel();

        if ( this.mChannel != null ) {
            Object ccb = this.mChannel.attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_CONTROL_BLOCK_KEY ) ).get();
            channel.attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_CONTROL_BLOCK_KEY ) ).set( ccb );
            WolfMCStandardConstants.copyChannelStandardAttrs( this.mChannel, channel );
        }

        return future;
    }

    public ArchUMCChannel    commitReconnectCandidate( Channel channel, ChannelFuture future ) {
        this.mLastChannelFuture = future;
        this.mChannel           = channel;
        this.mChannelID         = this.mChannel.id();

        return this;
    }

    public ArchUMCChannel    toConnect( SocketAddress address ) {
        this.mAddress           = address;
        this.mLastChannelFuture = this.getBootstrap().connect( address );

        Channel channel         = this.getLastChannelFuture().channel();
        if ( this.mChannel != null ) { // Reconnect
            Object ccb = this.mChannel.attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_CONTROL_BLOCK_KEY ) ).get();
            channel.attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_CONTROL_BLOCK_KEY ) ).set( ccb );
            WolfMCStandardConstants.copyChannelStandardAttrs( this.mChannel, channel );
        }
        this.mChannel           = channel;
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
