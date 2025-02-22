package com.pinecone.hydra.umc.wolfmc;

import com.pinecone.hydra.system.component.Slf4jTraceable;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.wolfmc.client.WolfMCClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
    public void              reconnect( long mils ) throws IOException {
        if( this.isShutdown() ) {
            ChannelFuture future = this.toConnect( this.getAddress() ).getLastChannelFuture();
            CompletableFuture<Void> completableFuture = new CompletableFuture<>();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete( ChannelFuture channelFuture ) throws Exception {
                    try {
                        completableFuture.complete( null );
                    }
                    catch (Exception e) {
                        completableFuture.completeExceptionally( e );
                    }
                }
            });

            try {
                if ( mils != -1 ) {
                   future.get( mils, TimeUnit.MILLISECONDS );
                }
                else {
                    future.get();
                }
            }
            catch ( InterruptedException e ) {
                Thread.currentThread().interrupt();
                throw new IOException( e );
            }
            catch ( TimeoutException | ExecutionException e ) {
                throw new IOException( e.getCause() );
            }


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

    public ArchUMCChannel    toConnect( SocketAddress address ){
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
