package com.pinecone.hydra.uma.wolf;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.google.protobuf.DynamicMessage;
import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.util.Debug;
import com.pinecone.hydra.uma.AppointClient;
import com.pinecone.hydra.uma.ArchAppointNode;
import com.pinecone.hydra.uma.AsynMsgHandler;
import com.pinecone.hydra.uma.AsynReturnHandler;
import com.pinecone.hydra.uma.proxy.GenericIfaceProxyFactory;
import com.pinecone.hydra.uma.proxy.IfaceProxyFactory;
import com.pinecone.hydra.servgram.Servgramium;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;
import com.pinecone.hydra.umc.msg.Medium;
import com.pinecone.hydra.umc.msg.Messenger;
import com.pinecone.hydra.umc.msg.UMCMessage;
import com.pinecone.hydra.umc.msg.event.ChannelDataInterceptor;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.msg.event.ChannelInactiveHandler;
import com.pinecone.hydra.umc.vita.HeartbeatControl;
import com.pinecone.hydra.umc.wolfmc.UlfInformMessage;
import com.pinecone.hydra.umc.wolfmc.UlfInstructMessage;
import com.pinecone.hydra.umc.wolfmc.client.ArchAsyncMessenger;
import com.pinecone.hydra.umc.wolfmc.client.ClientConnectArguments;
import com.pinecone.hydra.umc.wolfmc.client.UlfAsyncMessengerChannelControlBlock;
import com.pinecone.hydra.umc.wolfmc.client.UlfClient;
import com.pinecone.hydra.umc.wolfmc.client.WolfMCClient;
import com.pinecone.hydra.umct.DuplexExpress;
import com.pinecone.hydra.umct.IlleagalResponseException;
import com.pinecone.hydra.umct.husky.HuskyCTPConstants;
import com.pinecone.hydra.umct.husky.compiler.BytecodeIfacCompiler;
import com.pinecone.hydra.umct.husky.compiler.CompilerEncoder;
import com.pinecone.hydra.umct.husky.compiler.InterfacialCompiler;
import com.pinecone.hydra.umct.husky.compiler.MethodPrototype;
import com.pinecone.hydra.umct.husky.heartbeat.HuskyHeartbeatControl;
import com.pinecone.hydra.umct.husky.machinery.HuskyContextMachinery;
import com.pinecone.hydra.umct.mapping.BytecodeControllerInspector;
import com.pinecone.hydra.umct.mapping.ControllerInspector;
import com.pinecone.ulf.util.protobuf.GenericFieldProtobufDecoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import javassist.ClassPool;

/**
 *  Pinecone Ursus For Java WolfAppointClient [ Ulfhedinn Wolf RPC Client ]
 *  Bean Nuts Walnut Ulfhedinn Wolves/Ulfar Family.
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 */
public class WolfAppointClient extends ArchAppointNode implements AppointClient {
    protected UlfClient              mMessenger;

    protected IfaceProxyFactory      mIfaceProxyFactory;

    protected HeartbeatControl       mHeartbeatControl;

    protected boolean afterChannelInactive( ChannelControlBlock ccb ) throws ChannelHandleException {
        UlfAsyncMessengerChannelControlBlock cb = (UlfAsyncMessengerChannelControlBlock) ccb;
        Channel channel = cb.getChannel().getNativeHandle();
        WolfAppointClient.this.getLogger().info( "Proactive channel ({}), has detached.", channel.id() );
        UlfClient wrappedClient = WolfAppointClient.this.getMessageNode();
        if ( wrappedClient.getConnectionArguments().isAutoReconnect() ) {
            try {
                ArchAsyncMessenger.reconnect( cb, (Messenger) wrappedClient );

                WolfAppointClient.this.getLogger().info( "Proactive Channel ({}, `{}`), reconnect successfully.", channel.id(), cb.getChannel().getAddress() );
            }
            catch ( IOException e ) {
                WolfAppointClient.this.getLogger().error( "Proactive channel ({}), attempted to reconnect but failed.", channel.id(), e );
                throw new ChannelHandleException( e.getCause() );
            }
        }

        return true; // Blocking next inactive sequence.
    }

    protected void registerChannelInactiveHandler () {
        this.mMessenger.registerChannelInactiveHandler(new ChannelInactiveHandler() {
            @Override
            public boolean afterChannelInactive( ChannelControlBlock ccb ) throws ChannelHandleException {
                this.afterEventTriggered( ccb );

                return WolfAppointClient.this.afterChannelInactive( ccb );
            }
        });
    }

    protected void registerChannelConnectedHandler () {
        ClientConnectArguments arguments = WolfAppointClient.this.getMessageNode().getConnectionArguments();
        this.mMessenger.registerChannelConnectedHandler(new ChannelEventHandler() {
            @Override
            public void afterEventTriggered( ChannelControlBlock block ) {
                if ( arguments.isEnableHeartbeat() ) {
                    WolfAppointClient.this.mHeartbeatControl.registerChannel( block, arguments.getHeartbeatInterval() );
                }
            }
        });
    }

    protected void initUlfClientHeartbeatInterceptors( UlfClient client ) {
        client.registerArrivedDataInterceptor(new ChannelDataInterceptor() {
            @Override
            public boolean interceptAfterDataArrived( Medium medium, ChannelControlBlock block, UMCMessage msg, ChannelHandlerContext ctx, Object rawMsg ) {
                try {
                    return WolfAppointClient.this.mHeartbeatControl.interceptFeedback( block, msg );
                }
                catch ( IOException e ) {
                    throw new ProvokeHandleException( e );
                }
            }
        });
    }

    private void initSelf( UlfClient messenger ) {
        this.mMessenger            = messenger;
        this.mIfaceProxyFactory    = new GenericIfaceProxyFactory( this );

        ClientConnectArguments arguments = WolfAppointClient.this.getMessageNode().getConnectionArguments();
        if ( arguments.isEnableHeartbeat() ) {
            this.mHeartbeatControl = new HuskyHeartbeatControl( arguments.getHeartbeatInterval() );
            this.registerChannelConnectedHandler();
            this.initUlfClientHeartbeatInterceptors( messenger );
        }

        this.registerChannelInactiveHandler();
    }

    protected WolfAppointClient( UlfClient messenger, boolean delay ){
        super( (Servgramium) messenger );
        this.initSelf( messenger );
    }

    public WolfAppointClient( UlfClient messenger, InterfacialCompiler compiler, ControllerInspector controllerInspector ){
        this( messenger, true );
        this.mPMCTContextMachinery = new HuskyContextMachinery( compiler, controllerInspector, new GenericFieldProtobufDecoder() );
        this.initSelf( messenger );
    }

    public WolfAppointClient( UlfClient messenger, CompilerEncoder encoder ){
        this( messenger, new BytecodeIfacCompiler(
                ClassPool.getDefault(), messenger.getTaskManager().getClassLoader(), encoder
        ), new BytecodeControllerInspector(
                ClassPool.getDefault(), messenger.getTaskManager().getClassLoader()
        ) );
    }

    public WolfAppointClient( UlfClient messenger ){
        this( messenger, new BytecodeIfacCompiler(
                ClassPool.getDefault(), messenger.getTaskManager().getClassLoader()
        ), new BytecodeControllerInspector(
                ClassPool.getDefault(), messenger.getTaskManager().getClassLoader()
        ) );
    }


    @Override
    public void close() {
        this.mMessenger.close();
    }

    @Override
    public UlfClient getMessageNode() {
        return this.mMessenger;
    }


    @Override
    public UMCMessage sendSyncMsg( UMCMessage request ) throws IOException {
        return this.sendSyncMsg( request, false );
    }

    @Override
    public UMCMessage sendSyncMsg( UMCMessage request, boolean bNoneBuffered ) throws IOException {
        return this.mMessenger.sendSyncMsg( request, bNoneBuffered );
    }

    @Override
    public void sendAsynMsg( UMCMessage request ) throws IOException {
        this.mMessenger.sendAsynMsg( request );
    }

    @Override
    public void sendAsynMsg( UMCMessage request, AsynMsgHandler handler ) throws IOException {
        this.mMessenger.sendAsynMsg( request, AsynMsgHandler.wrap( handler ) );
    }


    @Override
    public void invokeInformAsyn( MethodPrototype method, Object[] args, AsynMsgHandler handler ) throws IOException {
        DynamicMessage message = this.reinterpretMsg( method, args );
        this.sendAsynMsg( new UlfInformMessage(message.toByteArray()), handler );
    }

    @Override
    public void invokeInformAsyn( MethodPrototype method, Object[] args, AsynReturnHandler handler ) throws IOException {
        DynamicMessage message = this.reinterpretMsg( method, args );
        this.sendAsynMsg(new UlfInformMessage(message.toByteArray()), new AsynMsgHandler() {
            @Override
            public void onSuccessfulMsgReceived( UMCMessage msg ) throws Exception {
                handler.onSuccessfulReturn( WolfAppointClient.this.unmarshalResponse( method, msg ) );
            }

            @Override
            public void onErrorMsgReceived( UMCMessage msg ) throws Exception {
                handler.onErrorMsgReceived( msg );
            }
        });
    }

    @Override
    public Object invokeInform( MethodPrototype method, Object[] args, long nWaitTimeMil ) throws IlleagalResponseException, IOException {
        CompletableFuture<Object> future = new CompletableFuture<>();
        DynamicMessage message = this.reinterpretMsg(method, args);

        this.sendAsynMsg(new UlfInformMessage(message.toByteArray()), new AsynMsgHandler() {
            @Override
            public void onSuccessfulMsgReceived( UMCMessage msg ) throws Exception {
                try {
                    Object result = WolfAppointClient.this.unmarshalResponse( method, msg );
                    future.complete(result);
                }
                catch ( IlleagalResponseException e ) {
                    future.completeExceptionally( e );
                }
            }

            @Override
            public void onErrorMsgReceived( UMCMessage msg ) throws Exception {
                future.completeExceptionally( new IlleagalResponseException( "Error message received: " + msg ) );
            }

            @Override
            public void onError( Object data, Throwable cause ) {
                future.completeExceptionally( cause );
            }
        });

        try {
            if ( nWaitTimeMil == -1 ) {
                if ( this.mMessenger instanceof WolfMCClient ) {
                    nWaitTimeMil = ((WolfMCClient) this.mMessenger).getConnectionArguments().getSyncWaitingMillis();
                }
            }

            return WolfAppointHelper.evalCompletableFuture( future, nWaitTimeMil );
        }
        catch ( TimeoutException | ExecutionException e ) {
            throw new IlleagalResponseException( e );
        }
        catch ( InterruptedException e ) {
            Thread.currentThread().interrupt();
            throw new IlleagalResponseException( e );
        }
    }

    @Override
    public Object invokeInform( MethodPrototype method, Object... args ) throws IlleagalResponseException, IOException {
        return this.invokeInform( method, args, -1 );
    }

    @Override
    public void invokeInformAsyn( String szMethodAddress, Object[] args, AsynMsgHandler handler ) throws IOException {
        this.invokeInformAsyn( this.queryMethodPrototype( szMethodAddress ), args, handler );
    }

    @Override
    public void invokeInformAsyn( String szMethodAddress, Object[] args, AsynReturnHandler handler ) throws IOException {
        this.invokeInformAsyn( this.queryMethodPrototype( szMethodAddress ), args, handler );
    }

    @Override
    public Object invokeInform( String szMethodAddress, Object[] args, long nWaitTimeMil ) throws IlleagalResponseException, IOException {
        return this.invokeInform( this.queryMethodPrototype( szMethodAddress ), args, nWaitTimeMil );
    }

    @Override
    public Object invokeInform( String szMethodAddress, Object... args ) throws IlleagalResponseException, IOException {
        return this.invokeInform( this.queryMethodPrototype( szMethodAddress ), args );
    }

    @Override
    public <T> T getIface( Class<T> iface ) {
        return this.mIfaceProxyFactory.createProxy( iface );
    }

}
