package com.pinecone.hydra.uma.wolf;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;

import com.pinecone.framework.unit.LinkedTreeMap;
import com.pinecone.hydra.uma.AppointServer;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.uma.UlfDuplexAppointClient;
import com.pinecone.hydra.umc.msg.ArchUMCProtocol;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;
import com.pinecone.hydra.umc.msg.ChannelPool;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.event.ChannelDataInterceptor;
import com.pinecone.hydra.umc.wolf.UlfAsyncMsgHandleAdapter;
import com.pinecone.hydra.umc.wolf.UlfChannel;
import com.pinecone.hydra.umc.wolf.UlfInstructMessage;
import com.pinecone.hydra.umc.wolf.WolfMCStandardConstants;
import com.pinecone.hydra.umc.wolf.client.UlfAsyncMessengerChannelControlBlock;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umc.wolf.client.WolfMCClient;
import com.pinecone.hydra.umc.wolf.client.reconnect.UlfReconnectFeature;
import com.pinecone.hydra.umct.DuplexExpress;
import com.pinecone.hydra.umct.MessageJunction;
import com.pinecone.hydra.umct.UMCTExpress;
import com.pinecone.hydra.umct.UMCTExpressHandler;
import com.pinecone.hydra.umct.husky.HuskyCTPConstants;
import com.pinecone.hydra.umct.husky.compiler.BytecodeIfaceCompiler;
import com.pinecone.hydra.umct.husky.compiler.CompilerEncoder;
import com.pinecone.hydra.umct.husky.compiler.ProtoInterfacialCompiler;
import com.pinecone.hydra.umct.husky.machinery.HuskyContextMachinery;
import com.pinecone.hydra.umct.husky.machinery.HuskyRouteDispatcher;
import com.pinecone.hydra.umct.husky.machinery.HuskyRouteDispatcherFabricator;
import com.pinecone.hydra.umct.husky.machinery.ProtoRouteDispatcher;
import com.pinecone.hydra.umct.husky.machinery.RouteDispatcher;
import com.pinecone.hydra.umct.mapping.BytecodeControllerInspector;
import com.pinecone.hydra.umct.mapping.ControllerInspector;
import com.pinecone.ulf.util.protobuf.GenericFieldProtobufDecoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import javassist.ClassPool;

/**
 *  Pinecone Ursus For Java WolvesAppointClient [ Ulfhedinn Wolf Duplex RPC Client ]
 *  Bean Nuts Walnut Ulfhedinn Wolves/Ulfar Family.
 *  Author: Harald.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 */
public class WolvesAppointClient extends WolfAppointClient implements UlfDuplexAppointClient {
    protected static Class<?> checkExpressType( Class<?> expressType ) {
        if ( !DuplexExpress.class.isAssignableFrom( expressType ) ) {
            throw new IllegalArgumentException( "`" + expressType.getSimpleName() + "` is not DuplexExpress calibre qualified." );
        }
        return expressType;
    }

    protected Map<ChannelId, ChannelControlBlock > mInstructedChannels;  // Standby controlled channels, waiting for server to instruct.
    protected RouteDispatcher                      mRouteDispatcher;
    protected PassiveChannelRegisterAckSupport     mPassiveRegisterAckSupport;
    protected ReentrantLock                        mPassiveChannelLock = new ReentrantLock();


    @Override
    protected boolean afterChannelInactive( ChannelControlBlock ccb, Object context ) throws ChannelHandleException {
        UlfAsyncMessengerChannelControlBlock cb = (UlfAsyncMessengerChannelControlBlock) ccb;
        Channel channel = cb.getChannel().getNativeHandle();
        Object ob = channel.attr( AttributeKey.valueOf( HuskyCTPConstants.HCTP_DUP_PASSIVE_CHANNEL_KEY ) ).get();
        if ( ob != null && (Boolean)ob ) {
            WolvesAppointClient.this.getLogger().info( "Passive-controlled channel ({}), has detached.", channel.id() );
            if ( this.mPassiveRegisterAckSupport != null ) {
                this.mPassiveRegisterAckSupport.cancel( channel );
            }
            UlfClient wrappedClient = WolvesAppointClient.this.getMessageNode();
            if ( wrappedClient instanceof WolfMCClient ) {
                WolfMCClient wolfClient = (WolfMCClient)wrappedClient;
                if ( wolfClient.isReconnectAllowed() ) {
                    wolfClient.getReconnectSupervisor().submit( cb, this.createDuplexReconnectFeature( wrappedClient ) );
                }
            }

            DuplexExpress express = (DuplexExpress)WolvesAppointClient.this.mRouteDispatcher.getUMCTExpress();
            express.afterChannelInactive( cb );
            return true; // Blocking next inactive sequence.
        }
        return super.afterChannelInactive( ccb, context );
    }

    protected UlfReconnectFeature createDuplexReconnectFeature( UlfClient wrappedClient ) {
        return new UlfReconnectFeature() {
            @Override
            public String name() {
                return "Bidirectional";
            }

            @Override
            public void afterReconnectSucceeded( ChannelControlBlock block, Channel oldChannel, Channel newChannel ) {

            }

            @Override
            public CompletableFuture<Void> afterReconnectCommitted( ChannelControlBlock block, Channel oldChannel, Channel newChannel ) throws IOException {
                WolvesAppointClient.copyDuplexAttrs( oldChannel, newChannel );
                wrappedClient.getChannelPool().remove( block );
                UlfInstructMessage instructMessage = new UlfInstructMessage( HuskyCTPConstants.HCTP_DUP_CONTROL_REGISTER );
                instructMessage.getHead().setIdentityId( wrappedClient.getMessageNodeId() );
                CompletableFuture<Void> ackFuture = WolvesAppointClient.this.mPassiveRegisterAckSupport.begin( newChannel );
                try {
                    WolvesAppointClient.this.getLogger().info(
                            "[PassiveChannelRegister] [Reconnect] Sending register frame. (Channel: `{}`, TransmitChannel: `{}`, Active: `{}`)",
                            new Object[]{ newChannel.id(), WolvesAppointClient.this.transmitChannelId( block ), newChannel.isActive() }
                    );
                    ( (UlfAsyncMessengerChannelControlBlock)block ).sendAsynMsg( instructMessage, true );
                    WolvesAppointClient.this.getLogger().info(
                            "[PassiveChannelRegister] [Reconnect] Register frame sent. (Channel: `{}`, TransmitChannel: `{}`, Active: `{}`)",
                            new Object[]{ newChannel.id(), WolvesAppointClient.this.transmitChannelId( block ), newChannel.isActive() }
                    );
                }
                catch ( IOException e ) {
                    WolvesAppointClient.this.mPassiveRegisterAckSupport.cancel( newChannel, ackFuture );
                    throw e;
                }

                return ackFuture;
            }
        };
    }

    private void initSelf() {
        this.mPassiveRegisterAckSupport = new PassiveChannelRegisterAckSupport();
        this.mPassiveRegisterAckSupport.registerInterceptor( this.mMessenger );
    }

    protected class PassiveChannelRegisterAckSupport {
        protected Map<ChannelId, CompletableFuture<Void> > mAckFutures = new ConcurrentHashMap<>();

        protected void registerInterceptor( UlfClient client ) {
            client.registerArrivedDataInterceptor(new ChannelDataInterceptor() {
                @Override
                public boolean interceptAfterDataArrived( Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) {
                    return PassiveChannelRegisterAckSupport.this.interceptAck( block, msg );
                }
            });
        }

        protected boolean interceptAck( ChannelControlBlock block, UMCMessage msg ) {
            if ( msg.getHead().getControlBits() != HuskyCTPConstants.HCTP_DUP_CONTROL_REGISTER_ACK ) {
                return false;
            }

            ChannelId channelId = (ChannelId) block.getChannel().getChannelID();
            CompletableFuture<Void> ackFuture = this.mAckFutures.remove( channelId );
            if ( ackFuture != null ) {
                WolvesAppointClient.this.getLogger().debug(
                        "[PassiveChannelRegister] Register ack received. (Channel: `{}`) <Acked>",
                        channelId
                );
                ackFuture.complete( null );
            }
            else {
                WolvesAppointClient.this.getLogger().debug(
                        "[PassiveChannelRegister] Register ack received without waiter. (Channel: `{}`) <Pass>",
                        channelId
                );
            }
            return true;
        }

        protected long getTimeoutMillis() {
            long nTimeoutMillis = WolvesAppointClient.this.mMessenger.getConnectionArguments().getSocketTimeout();
            return Math.max( nTimeoutMillis, 1000L );
        }

        protected CompletableFuture<Void> begin( Channel channel ) {
            ChannelId channelId = channel.id();
            CompletableFuture<Void> ackFuture = new CompletableFuture<>();
            CompletableFuture<Void> oldFuture = this.mAckFutures.put( channelId, ackFuture );
            if ( oldFuture != null ) {
                oldFuture.completeExceptionally( new IOException(
                        "Passive channel register ack superseded. Channel: " + channelId
                ) );
            }

            channel.eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    if ( PassiveChannelRegisterAckSupport.this.mAckFutures.remove( channelId, ackFuture ) ) {
                        IOException cause = new IOException(
                                "Waiting for passive channel register ack timeout. Channel: " + channelId
                        );
                        WolvesAppointClient.this.getLogger().warn(
                                "[PassiveChannelRegister] Register ack timeout. (Channel: `{}`) <AckTimeout>",
                                channelId
                        );
                        ackFuture.completeExceptionally( cause );
                    }
                }
            }, this.getTimeoutMillis(), TimeUnit.MILLISECONDS );

            return ackFuture;
        }

        protected void cancel( Channel channel, CompletableFuture<Void> ackFuture ) {
            if ( this.mAckFutures.remove( channel.id(), ackFuture ) ) {
                ackFuture.completeExceptionally( new IOException(
                        "Passive channel register ack cancelled. Channel: " + channel.id()
                ) );
            }
        }

        protected void cancel( Channel channel ) {
            CompletableFuture<Void> ackFuture = this.mAckFutures.remove( channel.id() );
            if ( ackFuture != null ) {
                WolvesAppointClient.this.getLogger().debug(
                        "[PassiveChannelRegister] Register ack waiter cancelled by channel detach. (Channel: `{}`)",
                        channel.id()
                );
                ackFuture.completeExceptionally( new IOException(
                        "Passive channel detached before register ack. Channel: " + channel.id()
                ) );
            }
        }

        protected void waitAck( Channel channel, CompletableFuture<Void> ackFuture ) throws IOException {
            try {
                ackFuture.get( this.getTimeoutMillis(), TimeUnit.MILLISECONDS );
            }
            catch ( InterruptedException e ) {
                Thread.currentThread().interrupt();
                throw new IOException( "Passive channel register ack interrupted. Channel: " + channel.id(), e );
            }
            catch ( ExecutionException e ) {
                throw new IOException( "Passive channel register ack failed. Channel: " + channel.id(), e.getCause() );
            }
            catch ( TimeoutException e ) {
                this.cancel( channel, ackFuture );
                throw new IOException( "Passive channel register ack timeout. Channel: " + channel.id(), e );
            }
        }
    }

    protected WolvesAppointClient( UlfClient messenger, ProtoRouteDispatcher dispatcher ) {
        super( messenger, dispatcher.getInterfacialCompiler(), dispatcher.getContextMachinery().getControllerInspector() );
        this.initSelf();
        this.mRouteDispatcher = dispatcher;
        this.mInstructedChannels = new LinkedTreeMap<>();
    }

    public WolvesAppointClient( UlfClient messenger, ProtoInterfacialCompiler compiler, ControllerInspector controllerInspector, UMCTExpress express ){
        this( messenger, new HuskyRouteDispatcher( compiler, controllerInspector, express ) );
        this.apply( express );
    }

    public WolvesAppointClient( UlfClient messenger, CompilerEncoder encoder, UMCTExpress express ){
        this( messenger, new HuskyRouteDispatcher( encoder, express, messenger.getTaskManager().getClassLoader() ) );
        this.apply( express );
    }

    public WolvesAppointClient( UlfClient messenger, UMCTExpress express ){
        this( messenger, new HuskyRouteDispatcher( express, messenger.getTaskManager().getClassLoader() ) );
        this.apply( express );
    }

    public WolvesAppointClient( UlfClient messenger, Class<?> expressType ){
        super( messenger, true );
        this.initSelf();

        try{
            Constructor<?> constructor = WolvesAppointClient.checkExpressType( expressType ).getConstructor( String.class, MessageJunction.class, Logger.class );
            UMCTExpress express = (UMCTExpress) constructor.newInstance( AppointServer.DefaultEntityName, this, this.getLogger() );

            this.mRouteDispatcher = new HuskyRouteDispatcher( express, messenger.getTaskManager().getClassLoader() );
            HuskyRouteDispatcherFabricator.afterConstructed( (HuskyRouteDispatcher)this.mRouteDispatcher, express );
            this.mMCTContextMachinery = new HuskyContextMachinery( new BytecodeIfaceCompiler(
                    ClassPool.getDefault(), messenger.getTaskManager().getClassLoader()
            ), new BytecodeControllerInspector(
                    ClassPool.getDefault(), messenger.getTaskManager().getClassLoader()
            ), new GenericFieldProtobufDecoder() );
            this.apply( express );
            this.mInstructedChannels = new LinkedTreeMap<>();
        }
        catch ( NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            throw new IllegalArgumentException( "`" + expressType.getSimpleName() + "` is not UMCTExpress calibre qualified." );
        }
    }

    public WolvesAppointClient( UlfClient messenger ){
        this( messenger, HuskyDuplexExpress.class );
    }



    protected static void copyDuplexAttrs( Channel leg, Channel neo ) {
        UlfChannel.copyChannelAttr( leg, neo, HuskyCTPConstants.HCTP_DUP_PASSIVE_CHANNEL_KEY );
    }

    public void apply( UMCTExpress handler ) {
        this.mRouteDispatcher.setUMCTExpress( handler );
    }

    @Override
    public RouteDispatcher getRouteDispatcher() {
        return this.mRouteDispatcher;
    }

    @Override
    public boolean supportDuplex() {
        return true;
    }

    @Override
    public void embraces( int nLine, UlfAsyncMsgHandleAdapter handler ) throws IOException {
        // Join us, embracing uniformity.

        this.mPassiveChannelLock.lock();
        try {
            this.createPassiveChannel0( nLine );
            this.registerPassiveChannels( handler );
        }
        finally {
            this.mPassiveChannelLock.unlock();
        }
    }

    public void rebuildPassiveChannels( int nLine, UlfAsyncMsgHandleAdapter handler ) throws IOException {
        this.mPassiveChannelLock.lock();
        try {
            this.ensurePassiveChannelLine( nLine );
            this.registerPassiveChannels( handler );
        }
        finally {
            this.mPassiveChannelLock.unlock();
        }
    }

    public void rebuildPassiveChannels( int nLine, UMCTExpressHandler handler ) throws IOException {
        this.rebuildPassiveChannels( nLine, UlfAsyncMsgHandleAdapter.wrap( handler ) );
    }

    public void rebuildPassiveChannels( int nLine ) throws IOException {
        this.rebuildPassiveChannels( nLine, this.mRouteDispatcher.getUMCTExpress() );
    }

    protected void registerPassiveChannels( UlfAsyncMsgHandleAdapter handler ) throws IOException {
        List<ChannelControlBlock> passiveChannels = new ArrayList<>( this.mInstructedChannels.values() );
        for ( ChannelControlBlock ccb : passiveChannels ) {
            this.reconnectPassiveChannelIfNeeded( ccb );
            this.refreshPassiveChannelKey( ccb );

            UlfInstructMessage instructMessage = new UlfInstructMessage( HuskyCTPConstants.HCTP_DUP_CONTROL_REGISTER );
            instructMessage.getHead().setIdentityId( this.mMessenger.getMessageNodeId() );

            UlfAsyncMessengerChannelControlBlock cb = (UlfAsyncMessengerChannelControlBlock) ccb;
            Channel channel = cb.getChannel().getNativeHandle();
            channel.attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_ASYNC_MSG_HANDLE_KEY ) ).set( handler );  // Exclusive handler.
            channel.attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_ASY_EXCLUSIVE_HANDLE_KEY ) ).set( true );
            channel.attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_EXTERNAL_CHANNEL_KEY ) ).set( true );
            channel.attr( AttributeKey.valueOf( HuskyCTPConstants.HCTP_DUP_PASSIVE_CHANNEL_KEY ) ).set( true );
            CompletableFuture<Void> ackFuture = this.mPassiveRegisterAckSupport.begin( channel );
            try {
                this.getLogger().info(
                        "[PassiveChannelRegister] Sending register frame. (Channel: `{}`, TransmitChannel: `{}`, Active: `{}`)",
                        new Object[]{ channel.id(), this.transmitChannelId( ccb ), channel.isActive() }
                );
                cb.sendAsynMsg( instructMessage, true );
                this.getLogger().info(
                        "[PassiveChannelRegister] Register frame sent. (Channel: `{}`, TransmitChannel: `{}`, Active: `{}`)",
                        new Object[]{ channel.id(), this.transmitChannelId( ccb ), channel.isActive() }
                );
            }
            catch ( IOException e ) {
                this.mPassiveRegisterAckSupport.cancel( channel, ackFuture );
                throw e;
            }

            this.getLogger().info( "Embracing and registering passive controlled channel ({}).", cb.getChannel().getNativeHandle().id() );
            this.mPassiveRegisterAckSupport.waitAck( channel, ackFuture );
        }
    }

    protected void reconnectPassiveChannelIfNeeded( ChannelControlBlock ccb ) throws IOException {
        if ( this.isPassiveChannelReconnecting( ccb ) ) {
            throw new IOException( "Passive channel is reconnecting. Channel: " + ccb.getChannel().getChannelID() );
        }
        if ( !ccb.isShutdown() ) {
            return;
        }

        ccb.getChannel().reconnect( this.mMessenger.getConnectionArguments().getSocketTimeout() );
    }

    protected Object transmitChannelId( ChannelControlBlock ccb ) {
        if ( ccb == null || !( ccb.getTransmit() instanceof ArchUMCProtocol ) ) {
            return "-";
        }

        Object nativeSource = ( (ArchUMCProtocol)ccb.getTransmit() ).getMessageSource().getNativeMessageSource();
        if ( nativeSource instanceof Channel ) {
            return ( (Channel)nativeSource ).id();
        }
        return nativeSource == null ? "null" : nativeSource.getClass().getSimpleName();
    }

    protected boolean isPassiveChannelReconnecting( ChannelControlBlock ccb ) {
        return this.mMessenger instanceof WolfMCClient
                && ( (WolfMCClient)this.mMessenger ).getReconnectSupervisor().isReconnecting( ccb );
    }

    protected void refreshPassiveChannelKey( ChannelControlBlock ccb ) {
        ChannelId id = (ChannelId)ccb.getChannel().getChannelID();
        this.mInstructedChannels.entrySet().removeIf( kv -> kv.getValue() == ccb && !kv.getKey().equals( id ) );
        this.mInstructedChannels.put( id, ccb );
    }

    protected void ensurePassiveChannelLine( int nLine ) {
        int nMissingLine = nLine - this.mInstructedChannels.size();
        if ( nMissingLine > 0 ) {
            this.createPassiveChannel0( nMissingLine );
        }
    }

    @Override
    public void embraces( int nLine, UMCTExpressHandler handler ) throws IOException {
        this.embraces( nLine, UlfAsyncMsgHandleAdapter.wrap( handler ) );
    }

    @Override
    public void embraces( int nLine ) throws IOException {
        this.embraces( nLine, this.mRouteDispatcher.getUMCTExpress() );
    }

    @Override
    public void createPassiveChannel( int nLine ) {
        this.mPassiveChannelLock.lock();
        try {
            this.createPassiveChannel0( nLine );
        }
        finally {
            this.mPassiveChannelLock.unlock();
        }
    }

    protected void createPassiveChannel0( int nLine ) {
        ChannelPool pool = this.getMessageNode().getChannelPool();

        ChannelControlBlock[] cbs = new ChannelControlBlock[ nLine ];
        for ( int i = 0; i < nLine; ++i ) {
            ChannelControlBlock ccb = pool.depriveIdleChannel();
            if ( ccb == null ) {
                for ( int j = 0; j < nLine; ++j ) {
                    if ( cbs[ j ] == null ) {
                        break;
                    }
                    ChannelId id = (ChannelId)cbs[ j ].getChannel().getChannelID();
                    this.mInstructedChannels.remove( id );
                    pool.add( cbs[ j ] );
                }
                throw new IllegalArgumentException( "Creating `PassiveChannel` is compromised due to insufficient free channels. Consider setting up sufficient parallel channels." );
            }

            ChannelId id = (ChannelId)ccb.getChannel().getChannelID();
            cbs[ i ] = ccb;
            this.mInstructedChannels.put( id, ccb );
        }
    }
}
