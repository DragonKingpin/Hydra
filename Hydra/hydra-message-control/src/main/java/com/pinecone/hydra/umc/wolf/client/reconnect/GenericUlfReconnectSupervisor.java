package com.pinecone.hydra.umc.wolf.client.reconnect;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.wolf.UlfChannel;
import com.pinecone.hydra.umc.wolf.client.MessengerNettyChannelControlBlock;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class GenericUlfReconnectSupervisor implements UlfReconnectSupervisor {
    protected WolfMCClient                                      mClient;
    protected UlfReconnectPolicy                                mPolicy;
    protected Map<ChannelControlBlock, UlfReconnectSession>     mSessions;

    public GenericUlfReconnectSupervisor( WolfMCClient client ) {
        this.mClient   = client;
        this.mPolicy   = new GenericUlfReconnectPolicy();
        this.mSessions = new ConcurrentHashMap<>();
    }

    @Override
    public void submit( ChannelControlBlock block, UlfReconnectFeature feature ) {
        if ( block == null || feature == null ) {
            return;
        }

        UlfReconnectSession session = new UlfReconnectSession( block, feature );
        UlfReconnectSession old     = this.mSessions.putIfAbsent( block, session );
        if ( old != null ) {
            this.mClient.getLogger().info(
                    "[ChannelReconnect] [{}] Channel ({}) already has active reconnect session. <Pass>",
                    feature.name(), block.getChannel().getChannelID()
            );
            return;
        }

        this.mClient.getLogger().info(
                "[ChannelReconnect] [{}] Channel ({}) detached, reconnect session scheduled. <Start>",
                feature.name(), block.getChannel().getChannelID()
        );
        this.schedule( session, 0 );
    }

    @Override
    public void clear() {
        this.mSessions.clear();
    }

    @Override
    public boolean isReconnecting( ChannelControlBlock block ) {
        return this.mSessions.containsKey( block );
    }

    protected void schedule( UlfReconnectSession session, long nDelayMillis ) {
        if ( this.mClient.isShutdown() || this.mClient.getEventLoopGroup() == null ) {
            this.mSessions.remove( session.mBlock );
            return;
        }

        try {
            this.mClient.getEventLoopGroup().schedule( new Runnable() {
                @Override
                public void run() {
                    GenericUlfReconnectSupervisor.this.attempt( session );
                }
            }, nDelayMillis, TimeUnit.MILLISECONDS );
        }
        catch ( RejectedExecutionException e ) {
            this.mSessions.remove( session.mBlock );
        }
    }

    protected void attempt( UlfReconnectSession session ) {
        if ( this.mSessions.get( session.mBlock ) != session ) {
            return;
        }

        if ( this.mClient.isShutdown() ) {
            this.mSessions.remove( session.mBlock );
            return;
        }

        session.mnAttempt++;

        ChannelControlBlock block = session.mBlock;
        UlfChannel ulfChannel     = (UlfChannel)block.getChannel();
        SocketAddress address     = ulfChannel.getAddress();
        Channel oldChannel        = ulfChannel.getNativeHandle();
        session.mConnectionArriveFuture = this.prepareConnectionArriveFuture( block );
        this.mClient.getLogger().info(
                "[ChannelReconnect] [{}] Attempt {} started. (Channel: `{}`, Addr: `{}`)",
                session.mFeature.name(), session.mnAttempt, oldChannel.id(), address
        );

        ChannelFuture future = ulfChannel.openReconnectCandidate( address );
        this.expectConnectionArriveChannel( block, future.channel() );
        future.addListener( new ChannelFutureListener() {
            @Override
            public void operationComplete( ChannelFuture completedFuture ) throws Exception {
                if ( completedFuture.isSuccess() ) {
                    GenericUlfReconnectSupervisor.this.completeSucceeded( session, oldChannel, future );
                }
                else {
                    completedFuture.channel().close();
                    GenericUlfReconnectSupervisor.this.completeFailed( session, completedFuture.cause() );
                }
            }
        } );
    }

    protected void completeSucceeded( UlfReconnectSession session, Channel oldChannel, ChannelFuture future ) {
        if ( this.mSessions.get( session.mBlock ) != session ) {
            future.channel().close();
            return;
        }

        ChannelControlBlock block = session.mBlock;
        UlfChannel ulfChannel     = (UlfChannel)block.getChannel();

        try {
            ulfChannel.commitReconnectCandidate( future.channel(), future );
            this.mClient.getChannelPool().replaceChannel( oldChannel.id(), block );
            Channel newChannel = future.channel();
            session.mConnectionArriveFuture.whenComplete( ( activeValue, activeCause ) -> {
                if ( activeCause != null ) {
                    GenericUlfReconnectSupervisor.this.completeReadyFailed( session, activeCause );
                    return;
                }

                CompletableFuture<Void> readyFuture;
                try {
                    readyFuture = session.mFeature.afterReconnectCommitted( block, oldChannel, newChannel );
                    if ( readyFuture == null ) {
                        readyFuture = CompletableFuture.completedFuture( null );
                    }
                }
                catch ( Exception e ) {
                    GenericUlfReconnectSupervisor.this.completeReadyFailed( session, e );
                    return;
                }

                readyFuture.whenComplete( ( value, cause ) -> {
                    if ( cause == null ) {
                        GenericUlfReconnectSupervisor.this.completeReady( session, oldChannel, newChannel );
                    }
                    else {
                        GenericUlfReconnectSupervisor.this.completeReadyFailed( session, cause );
                    }
                } );
            } );
        }
        catch ( Exception e ) {
            try {
                block.close();
            }
            catch ( Exception ignore ) {
                // Ignore close failure during reconnect recovery.
            }
            this.completeFailed( session, e );
        }
    }

    protected void completeReady( UlfReconnectSession session, Channel oldChannel, Channel newChannel ) {
        if ( this.mSessions.get( session.mBlock ) != session ) {
            return;
        }

        ChannelControlBlock block = session.mBlock;
        try {
            this.mSessions.remove( block );
            this.mClient.notifyReconnectChannelConnected( block );
        }
        catch ( Exception e ) {
            this.completeReadyFailed( session, e );
            return;
        }

        this.mClient.getLogger().info(
                "[ChannelReconnect] [{}] Attempt {} succeeded. (OldChannel: `{}`, NewChannel: `{}`) <Done>",
                session.mFeature.name(), session.mnAttempt, oldChannel.id(), newChannel.id()
        );
    }

    protected void completeReadyFailed( UlfReconnectSession session, Throwable cause ) {
        if ( this.mSessions.get( session.mBlock ) != session ) {
            return;
        }

        try {
            session.mBlock.close();
        }
        catch ( Exception ignore ) {
            // Ignore close failure during reconnect recovery.
        }
        this.completeFailed( session, cause );
    }

    protected void completeFailed( UlfReconnectSession session, Throwable cause ) {
        if ( this.mSessions.get( session.mBlock ) != session ) {
            return;
        }

        if ( !this.mPolicy.shouldContinue( session.mnAttempt ) ) {
            this.mSessions.remove( session.mBlock );
            this.mClient.getLogger().error(
                    "[ChannelReconnect] [{}] Attempt {} failed and abandoned.",
                    session.mFeature.name(), session.mnAttempt, cause
            );
            return;
        }

        long nDelayMillis = this.mPolicy.nextDelayMillis( session.mnAttempt );
        this.mClient.getLogger().warn(
                "[ChannelReconnect] [{}] Attempt {} failed, next attempt after {} ms.",
                session.mFeature.name(), session.mnAttempt, nDelayMillis, cause
        );
        this.schedule( session, nDelayMillis );
    }

    protected CompletableFuture<Void> prepareConnectionArriveFuture( ChannelControlBlock block ) {
        if ( block instanceof MessengerNettyChannelControlBlock ) {
            return ( (MessengerNettyChannelControlBlock)block ).prepareConnectionArriveFuture();
        }
        return CompletableFuture.completedFuture( null );
    }

    protected void expectConnectionArriveChannel( ChannelControlBlock block, Channel expectedChannel ) {
        if ( block instanceof MessengerNettyChannelControlBlock ) {
            ( (MessengerNettyChannelControlBlock)block ).expectConnectionArriveChannel( expectedChannel );
        }
    }

    protected static class UlfReconnectSession {
        protected ChannelControlBlock   mBlock;
        protected UlfReconnectFeature   mFeature;
        protected int                   mnAttempt;
        protected CompletableFuture<Void> mConnectionArriveFuture;

        protected UlfReconnectSession( ChannelControlBlock block, UlfReconnectFeature feature ) {
            this.mBlock     = block;
            this.mFeature   = feature;
            this.mnAttempt  = 0;
        }
    }
}
