package com.pinecone.hydra.umc.wolf.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;

import com.pinecone.framework.system.IrrationalProvokedException;
import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.umc.msg.Messagus;
import com.pinecone.hydra.umc.msg.UMCServiceException;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.wolf.AsyncUlfMedium;
import com.pinecone.hydra.umc.msg.event.ChannelInactiveHandler;
import com.pinecone.hydra.umc.wolf.ChannelUtils;
import com.pinecone.hydra.umc.wolf.GenericUMCByteMessageDecoder;
import com.pinecone.hydra.umc.wolf.MCSecurityAuthentication;
import com.pinecone.hydra.umc.wolf.UlfAsyncMsgHandleAdapter;
import com.pinecone.hydra.umc.wolf.UlfChannel;
import com.pinecone.hydra.umc.wolf.UlfChannelStatus;
import com.pinecone.hydra.umc.wolf.UlfMCReceiver;
import com.pinecone.hydra.umc.wolf.UlfMessageNode;
import com.pinecone.hydra.umc.wolf.UnsetUlfAsyncMsgHandleAdapter;
import com.pinecone.hydra.umc.wolf.WolfMCInitializationException;
import com.pinecone.hydra.umc.wolf.WolfMCStandardConstants;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.extra.ExtraHeadCoder;
import com.pinecone.hydra.umct.UMCTExpressHandler;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 *  Pinecone Ursus For Java WolfClient [ Wolf, Uniform Message Control Protocol Client ]
 *  Author: Harald.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 *  Bean Nuts Walnut Ulfhedinn Wolves/Ulfar Family.
 *  Uniform Message Control Protocol (UMC)
 *    UMC is a simple TCP/IP-based binary transmission protocol.
 *    It supports methods similar to PUT/POST (HTTP), which are designed to fulfill uniform message control.
 *
 *  Uniform Message Control Protocol for WolfMC Service [Client/Server] (Ulf UMC)
 *  *****************************************************************************************
 */
public class WolfMCClient extends ArchAsyncMessenger implements UlfClient {
    protected EventLoopGroup                       mExecutorGroup;
    protected Bootstrap                            mBootstrap;

    protected ClientConnectArguments               mConnectionArguments;
    protected MCSecurityAuthentication             mSecurityAuthentication; //TODO

    protected UlfAsyncMsgHandleAdapter             mPrimeAsyncMessageHandler = new UnsetUlfAsyncMsgHandleAdapter( this ); // For all channels.

    protected List<ChannelEventHandler>            mChannelConnectedHandlers = new ArrayList<>();

    public WolfMCClient( long nodeId, String szName, Processum parentProcess, UlfMessageNode parent, Map<String, Object> joConf, ExtraHeadCoder extraHeadCoder ){
        super( nodeId, szName, parentProcess, parent, joConf, extraHeadCoder );

        this.apply( joConf );
    }

    public WolfMCClient( String szName, Processum parentProcess, UlfMessageNode parent, Map<String, Object> joConf, ExtraHeadCoder extraHeadCoder ){
        super( Messagus.nextLocalId(), szName, parentProcess, parent, joConf, extraHeadCoder );

        this.apply( joConf );
    }

    public WolfMCClient( long nodeId, String szName, Processum parentProcess, Map<String, Object> joConf, ExtraHeadCoder extraHeadCoder ){
        this( nodeId, szName, parentProcess, null, joConf, extraHeadCoder );
    }

    public WolfMCClient( String szName, Processum parentProcess, Map<String, Object>  joConf, ExtraHeadCoder extraHeadCoder ){
        this( Messagus.nextLocalId(), szName, parentProcess, null, joConf, extraHeadCoder );
    }

    public WolfMCClient( long nodeId, String szName, Processum parentProcess, Map<String, Object>  joConf ){
        this( nodeId, szName, parentProcess, joConf, null );
    }

    public WolfMCClient( String szName, Processum parentProcess, Map<String, Object>  joConf ){
        this( Messagus.nextLocalId(), szName, parentProcess, joConf, null );
    }

    public WolfMCClient( long nodeId, String szName, UlfMessageNode parent, Processum parentProcess, Map<String, Object>  joConf ){
        this( nodeId, szName, parentProcess, parent, joConf, null );
    }

    public WolfMCClient( String szName, UlfMessageNode parent, Processum parentProcess, Map<String, Object>  joConf ){
        this( Messagus.nextLocalId(), szName, parentProcess, parent, joConf, null );
    }

    protected WolfMCClient( Builder builder ){
        this( builder.nodeId, builder.szName, builder.parentProcess, builder.parent, builder.joConf, builder.extraHeadCoder );
    }


    @Override
    public UlfClient                      registerChannelConnectedHandler( ChannelEventHandler handler ) throws IllegalStateException {
        this.checkDeregisterHandlerStatus();
        this.mChannelConnectedHandlers.add( handler );
        return this;
    }

    @Override
    public UlfClient                      deregisterChannelConnectedHandler( ChannelEventHandler handler ) throws IllegalStateException {
        this.checkDeregisterHandlerStatus();
        this.mChannelConnectedHandlers.remove( handler );
        return this;
    }

    @Override
    public WolfMCClient                   apply( Map<String, Object>  joConf ) {
        super.apply( joConf );
        this.mConnectionArguments = new ClientConnectionArguments( this.getSectionConf() );

        return this;
    }

    @Override
    public WolfMCClient                   apply( UlfAsyncMsgHandleAdapter fnAsyncMessageAdapter ) {
        this.mPrimeAsyncMessageHandler = fnAsyncMessageAdapter;

        return this;
    }

    @Override
    public UMCTExpressHandler             getAsyncMsgHandler() {
        return this.mPrimeAsyncMessageHandler;
    }

    @Override
    public ClientConnectArguments         getConnectionArguments() {
        return this.mConnectionArguments;
    }

    @Override
    public ClientConnectArguments         getMessageNodeConfig() {
        return this.getConnectionArguments();
    }

    public EventLoopGroup                 getEventLoopGroup() {
        return this.mExecutorGroup;
    }

    public Bootstrap                      getBootstrap() {
        return this.mBootstrap;
    }

    public int                            getParallelChannels() {
        return this.getConnectionArguments().getParallelChannels();
    }

    protected void                        clear(){
        this.mChannelPool.clear();
    }

    @Override
    public void                           close() throws ProvokeHandleException {
        this.mStateMutex.lock();
        try {
            if( this.mExecutorGroup != null ) {
                this.mExecutorGroup.shutdownGracefully();
                this.clear();
                this.mExecutorGroup = null;
            }
        }
        finally {
            this.mStateMutex.unlock();
        }

        try {
            synchronized ( this.mPrimaryThreadJoinMutex ) {
                WolfMCClient.this.mPrimaryThreadJoinMutex.notify();
            }
        }
        catch ( IllegalMonitorStateException e ) {
            throw new ProvokeHandleException( "IllegalMonitorStateException [WolfMCClient::close], this exception has been redirected to parent thread.", e );
        }
    }

    @Override
    public void                           kill() {
        try {
            this.close();
        }
        catch ( ProvokeHandleException e ) {
            super.kill(); // Kill master thread forcefully.
            this.clear();
        }
    }

    @Override
    public boolean                        isShutdown() {
        if ( this.mExecutorGroup == null ) {
            return true;
        }
        return this.mExecutorGroup.isShutdown();
    }

    @Override
    public boolean                        isTerminated() {
        if ( this.mExecutorGroup == null ) {
            return true;
        }
        return this.mExecutorGroup.isTerminated();
    }

    protected void                        notifyChannelConnected( ChannelControlBlock block, ChannelHandlerContext ctx ) {
        for( ChannelEventHandler h : this.mChannelConnectedHandlers ) {
            h.afterEventTriggered( block, ctx );
        }
    }

    protected MessengerNettyChannelControlBlock syncSpawnSoloChannel() throws IOException, UMCServiceException {
        MessengerNettyChannelControlBlock ccb = null;
        ccb                                   = new MessengerNettyChannelControlBlock( this );
        ChannelFuture future                  = ccb.getChannel().toConnect(
                new InetSocketAddress( this.getConnectionArguments().getHost(), this.getConnectionArguments().getPort() )
        ).getLastChannelFuture();
        UlfChannel channel = ccb.getChannel();
        channel.getNativeHandle().attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_CONTROL_BLOCK_KEY ) ).set( ccb );
        ChannelUtils.setChannelIdentityID( channel, this.mnMessageNodeId );

        this.getTaskManager().add( ccb );

        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete( ChannelFuture channelFuture ) throws Exception {
                synchronized ( WolfMCClient.this.mPrimaryThreadJoinMutex ) {
//                    if ( WolfMCClient.this.isShutdown() ) {
//                        WolfMCClient.this.mShutdown = !channelFuture.isSuccess();
//                    }
                    WolfMCClient.this.mPrimaryThreadJoinMutex.notify();
                }
            }
        });
        //channel.closeFuture().sync();
        this.getChannelPool().pushBack( ccb );

        synchronized ( this.mPrimaryThreadJoinMutex ) {
            try {
                this.mPrimaryThreadJoinMutex.wait( this.getConnectionArguments().getSocketTimeout() );
                if( WolfMCClient.this.isShutdown() ) {
                    throw new UnknownHostException( "Connect failed with '" + this.getConnectionArguments().getHost() + ":" + this.getConnectionArguments().getPort() + "'" );
                }
            }
            catch ( InterruptedException e ) {
                Thread.currentThread().interrupt();
                throw new WolfMCInitializationException( e );
            }
        }

        this.notifyChannelConnected( ccb, null );
        return ccb;
    }

    protected void                        syncSpawnChannels() throws IOException, UMCServiceException {
        int n = this.getConnectionArguments().getParallelChannels();

        for ( int i = 0; i < n; i++ ) {
            MessengerNettyChannelControlBlock block = this.syncSpawnSoloChannel();
            this.infoLifecycle( String.format( "Channel%d(%s)", i, block.getChannel().getChannelID() ), "Spawned" );
        }
    }

    protected void                        invokeChannelOwnedOnError( ChannelHandlerContext ctx, Throwable cause ) {
        try {
            UlfAsyncMsgHandleAdapter handle = (UlfAsyncMsgHandleAdapter)ctx.channel().attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_ASYNC_MSG_HANDLE_KEY ) ).get();
            if( handle == null ) {
                ChannelControlBlock ccb = (ChannelControlBlock)ctx.channel().attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_CONTROL_BLOCK_KEY ) ).get();
                handle = ccb.pollMsgHandle( ArchAsyncMessenger.getSyncWaitingMillis( this ) );
                if( handle == null ) {
                    handle = WolfMCClient.this.mPrimeAsyncMessageHandler;
                }
            }
            handle.onError( ctx, cause );
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
        }
    }

    protected void                        handleArrivedMessage( UlfAsyncMsgHandleAdapter handle, Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) throws Exception {
        if( this.getErrorMessageAudit().isErrorMessage( msg ) ) {
            handle.onErrorMsgReceived( medium, block, msg, ctx, msg );
        }
        else {
            handle.onSuccessfulMsgReceived( medium, block, msg, ctx, msg );
        }
    }

    protected void                        shutdownIfAllChannelDetached ( MessengerNettyChannelControlBlock ccb ) {
        if ( !WolfMCClient.this.getConnectionArguments().isAutoReconnect() ) {
            if( WolfMCClient.this.getChannelPool().isAllChannelsTerminated() ) {
                try{
                    WolfMCClient.this.getLogger().warn( "<AutoReconnection is disabled> All channels are terminated, client terminating." );
                    WolfMCClient.this.close();
                }
                catch ( ProvokeHandleException e ) {
                    throw new IrrationalProvokedException( e ); // Those should never have happened.
                }

                return;
            }

            WolfMCClient.this.getChannelPool().deactivate( ccb );
            WolfMCClient.this.getMajorIOLock().lock();
            try{
                WolfMCClient.this.getTaskManager().erase( ccb );
            }
            finally {
                WolfMCClient.this.getMajorIOLock().unlock();
            }
        }
    }

    protected void                        initNettySubsystem() throws IOException, UMCServiceException {
        this.mExecutorGroup = new NioEventLoopGroup();
        this.mBootstrap     = new Bootstrap();
        Bootstrap bootstrap = this.mBootstrap;
        bootstrap.group  ( this.mExecutorGroup    );
        bootstrap.channel( NioSocketChannel.class );
        bootstrap.option ( ChannelOption.CONNECT_TIMEOUT_MILLIS, this.getConnectionArguments().getSocketTimeout() );
        bootstrap.handler( new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel( SocketChannel sc ) throws Exception {
                sc.pipeline().addLast( new ReadTimeoutHandler( WolfMCClient.this.getConnectionArguments().getKeepAliveTimeout(), TimeUnit.SECONDS ) );
                sc.pipeline().addLast( new GenericUMCByteMessageDecoder( WolfMCClient.this.getExtraHeadCoder() ) );
                sc.pipeline().addLast( new ChannelInboundHandlerAdapter (){
                    @Override
                    public void channelActive( ChannelHandlerContext ctx ) throws Exception {
                        super.channelActive(ctx);

                        //UlfChannelControlBlock channel = WolfMCClient.this.getChannelPool().queryChannelById( ctx.channel().id() );
                        MessengerNettyChannelControlBlock channel = (MessengerNettyChannelControlBlock)ctx.channel().attr(
                                AttributeKey.valueOf(WolfMCStandardConstants.CB_CONTROL_BLOCK_KEY)
                        ).get();

                        channel.afterConnectionArrive(
                                new AsyncUlfMedium( ctx, null, WolfMCClient.this ),  false
                        );
                        channel.setThreadAffinity( Thread.currentThread() );
                        synchronized ( WolfMCClient.this.mPrimaryThreadJoinMutex ) {
                            WolfMCClient.this.mPrimaryThreadJoinMutex.notify();
                        }
                    }

                    @Override
                    public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception {
                        Medium medium          = new AsyncUlfMedium( ctx, (ByteBuf) msg, WolfMCClient.this );
                        UlfMCReceiver receiver = new UlfMCReceiver( medium );
                        UMCMessage message     = receiver.readMsg();

                        MessengerNettyChannelControlBlock channelControlBlock = (MessengerNettyChannelControlBlock)ctx.channel().attr(
                                AttributeKey.valueOf( WolfMCStandardConstants.CB_CONTROL_BLOCK_KEY )
                        ).get();


                        //Debug.trace( channelControlBlock.getChannel().getChannelID() );
                        if( channelControlBlock.getChannelStatus() == UlfChannelStatus.FORCE_SYNCHRONIZED ){
                            channelControlBlock.getSyncRetMsgQueue().add( message );
                            //WolfMCClient.this.mSyncRetMsgQueue.add( message );
                        }
                        else {
                            if ( !WolfMCClient.this.tryInvokeOrInterceptArrivedData( medium, channelControlBlock, message, ctx, msg ) ) {
                                UlfAsyncMsgHandleAdapter handle = (UlfAsyncMsgHandleAdapter)ctx.channel().attr(
                                        AttributeKey.valueOf( WolfMCStandardConstants.CB_ASYNC_MSG_HANDLE_KEY )
                                ).get();
                                if ( handle == null ) {
                                    handle = channelControlBlock.pollMsgHandle( WolfMCClient.this.getSyncWaitingMillis() ); // Try pipeline.
                                }

                                if( handle != null ) {
                                    WolfMCClient.this.handleArrivedMessage( handle, medium, channelControlBlock, message, ctx, msg );

                                    // Preserving binding-status for exclusive handler-binding channel.
                                    Object dyAsynExclusiveHandle = ctx.channel().attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_ASY_EXCLUSIVE_HANDLE_KEY ) ).get();
                                    if ( dyAsynExclusiveHandle == null || !(Boolean) dyAsynExclusiveHandle ){
                                        ctx.channel().attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_ASYNC_MSG_HANDLE_KEY ) ).set( null ); // For another channel to reset, likes ajax.
                                    }
                                }
                                else {
                                    WolfMCClient.this.handleArrivedMessage( WolfMCClient.this.mPrimeAsyncMessageHandler, medium, channelControlBlock, message, ctx, msg );
                                }
                            }

                            Object dyExternalChannel = ctx.channel().attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_EXTERNAL_CHANNEL_KEY ) ).get();
                            if ( dyExternalChannel == null || !(Boolean) dyExternalChannel ){
                                WolfMCClient.this.getChannelPool().setIdleChannel( channelControlBlock );
                            }
                        }

                        medium.release();
                        medium = new AsyncUlfMedium( ctx, null, WolfMCClient.this );
                        channelControlBlock.afterConnectionArrive( medium,  true );
                    }

                    @Override
                    public void channelInactive( ChannelHandlerContext ctx ) throws Exception {
                        MessengerNettyChannelControlBlock ccb = (MessengerNettyChannelControlBlock)ctx.channel().attr(
                                AttributeKey.valueOf( WolfMCStandardConstants.CB_CONTROL_BLOCK_KEY )
                        ).get();

                        if ( !WolfMCClient.this.mChannelInactiveHandlers.isEmpty() ) {
                            boolean bBlocked = false;
                            for ( ChannelInactiveHandler handler : WolfMCClient.this.mChannelInactiveHandlers ) {
                                if ( handler.afterChannelInactive( ccb, ctx ) ) {
                                    bBlocked = true;
                                }
                            }

                            if ( bBlocked ) {
                                WolfMCClient.this.shutdownIfAllChannelDetached( ccb );
                                return;
                            }
                        }

                        WolfMCClient.this.shutdownIfAllChannelDetached( ccb );
                    }

                    @Override
                    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
                        WolfMCClient.this.invokeChannelOwnedOnError( ctx, cause );
                    }
                } );
            }

            @Override
            public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ) throws Exception {
                WolfMCClient.this.invokeChannelOwnedOnError( ctx, cause );
            }
        });

        this.syncSpawnChannels();
        this.infoLifecycle( "Wolf<\uD83D\uDC3A>::initNettySubsystem", "Ready" );
    }

    public void                           connect() throws IOException, UMCServiceException {
        this.mStateMutex.lock();

        try{
            if( this.isShutdown() ) {
                this.initNettySubsystem(); // Exception thrown and truncating next detach-mutex-release, redirecting to primary thread.
            }
        }
        finally {
            this.mStateMutex.unlock();
            WolfMCClient.this.unlockOuterThreadDetachMutex();  // This lock shouldn`t be released in `finally`, waiting for primary thread to process.
        }

        synchronized ( this.mPrimaryThreadJoinMutex ) {
            try {
                this.mPrimaryThreadJoinMutex.wait( ); // Join the primary thread.
            }
            catch ( InterruptedException e ) {
                Thread.currentThread().interrupt();
                throw new WolfMCInitializationException( e );
            }
        }
    }

    @Override
    public void                           execute() throws UMCServiceException {
        if ( !this.isShutdown() ) {
            this.mLogger.info( "WolfMCClient [{}:{}] is already started. <Pass>", this.getName(), this.hashCode() );
            return;
        }

        Exception[] lastException = new Exception[] { null };
        Thread primaryThread      = new Thread( new Runnable() {
            @Override
            public void run() {
                WolfMCClient.this.getTaskManager().notifyExecuting( WolfMCClient.this );
                try{
                    WolfMCClient.this.connect();
                }
                catch ( Exception e ) {
                    lastException[0] = e;
                    WolfMCClient.this.kill();
                }
                finally {
                    WolfMCClient.this.getTaskManager().notifyFinished( WolfMCClient.this );
                    WolfMCClient.this.unlockOuterThreadDetachMutex();
                }
            }
        });

        this.preparePrimaryThread( primaryThread );
        primaryThread.start();

        this.joinOuterThread();

        try {
            this.redirectException2ParentThread( lastException[0] );
        }
        catch ( IOException e ) {
            throw new WolfMCInitializationException( e );
        }
    }

    @Override
    public UMCMessage                     sendSyncMsg( UMCMessage request ) throws IOException {
        return this.sendSyncMsg( request, false );
    }

    @Override
    public UMCMessage                     sendSyncMsg( UMCMessage request, boolean bNoneBuffered ) throws IOException {
        return this.sendSyncMsg( request, bNoneBuffered, this.getConnectionArguments().getSyncWaitingMillis() );
    }

    @Override
    public void                           sendAsynMsg( UMCMessage request ) throws IOException {
        this.sendAsynMsg( request, false );
    }

    @Override
    public void                           sendAsynMsg( UMCMessage request, UlfAsyncMsgHandleAdapter handler ) throws IOException {
        this.sendAsynMsg( request, false, handler );
    }




    public static class Builder {
        private long                nodeId = -1;
        private String              szName;
        private Processum           parentProcess;
        private UlfMessageNode      parent;
        private Map<String, Object> joConf;
        private ExtraHeadCoder      extraHeadCoder;

        public Builder setNodeId( long nodeId ) {
            this.nodeId = nodeId;
            return this;
        }

        public Builder setName( String szName ) {
            this.szName = szName;
            return this;
        }

        public Builder setParentProcess( Processum parentProcess ) {
            this.parentProcess = parentProcess;
            return this;
        }

        public Builder setParent( UlfMessageNode parent ) {
            this.parent = parent;
            return this;
        }

        public Builder setJoConf( Map<String, Object> joConf ) {
            this.joConf = joConf;
            return this;
        }

        public Builder setExtraHeadCoder( ExtraHeadCoder extraHeadCoder ) {
            this.extraHeadCoder = extraHeadCoder;
            return this;
        }

        public WolfMCClient build() {
            this.validate();
            return new WolfMCClient(this);
        }

        private void validate() {
            if ( this.szName == null || this.szName.isEmpty() ) {
                long nId = this.nodeId;
                if ( nId == -1 ) {
                    nId = System.nanoTime();
                }
                this.szName = WolfMCClient.class.getSimpleName() + "_" + nId;
            }
            if ( this.joConf == null ) {
                throw new IllegalArgumentException( "Configuration (Conf) cannot be null" );
            }
        }
    }


}
