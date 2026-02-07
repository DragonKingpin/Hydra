package com.pinecone.hydra.uma.wolf;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.slf4j.Logger;

import com.pinecone.framework.unit.LinkedTreeMap;
import com.pinecone.hydra.uma.AppointServer;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.HuskyDuplexExpress;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;
import com.pinecone.hydra.umc.msg.ChannelPool;
import com.pinecone.hydra.umc.msg.MediumTerminationException;
import com.pinecone.hydra.umc.msg.Messenger;
import com.pinecone.hydra.umc.wolf.UlfAsyncMsgHandleAdapter;
import com.pinecone.hydra.umc.wolf.UlfChannel;
import com.pinecone.hydra.umc.wolf.UlfInstructMessage;
import com.pinecone.hydra.umc.wolf.WolfMCStandardConstants;
import com.pinecone.hydra.umc.wolf.client.ArchAsyncMessenger;
import com.pinecone.hydra.umc.wolf.client.UlfAsyncMessengerChannelControlBlock;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import com.pinecone.hydra.umct.DuplexExpress;
import com.pinecone.hydra.umct.MessageJunction;
import com.pinecone.hydra.umct.UMCTExpress;
import com.pinecone.hydra.umct.UMCTExpressHandler;
import com.pinecone.hydra.umct.husky.HuskyCTPConstants;
import com.pinecone.hydra.umct.husky.compiler.BytecodeIfacCompiler;
import com.pinecone.hydra.umct.husky.compiler.CompilerEncoder;
import com.pinecone.hydra.umct.husky.compiler.InterfacialCompiler;
import com.pinecone.hydra.umct.husky.machinery.HuskyContextMachinery;
import com.pinecone.hydra.umct.husky.machinery.HuskyRouteDispatcher;
import com.pinecone.hydra.umct.husky.machinery.HuskyRouteDispatcherFabricator;
import com.pinecone.hydra.umct.husky.machinery.RouteDispatcher;
import com.pinecone.hydra.umct.mapping.BytecodeControllerInspector;
import com.pinecone.hydra.umct.mapping.ControllerInspector;
import com.pinecone.ulf.util.protobuf.GenericFieldProtobufDecoder;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.AttributeKey;
import javassist.ClassPool;

/**
 *  Pinecone Ursus For Java WolvesAppointClient [ Ulfhedinn Wolf Duplex RPC Client ]
 *  Bean Nuts Walnut Ulfhedinn Wolves/Ulfar Family.
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 */
public class WolvesAppointClient extends WolfAppointClient implements DuplexAppointClient {
    protected static Class<?> checkExpressType( Class<?> expressType ) {
        if ( !DuplexExpress.class.isAssignableFrom( expressType ) ) {
            throw new IllegalArgumentException( "`" + expressType.getSimpleName() + "` is not DuplexExpress calibre qualified." );
        }
        return expressType;
    }

    protected Map<ChannelId, ChannelControlBlock > mInstructedChannels;  // Standby controlled channels, waiting for server to instruct.
    protected RouteDispatcher                      mRouteDispatcher;


    @Override
    protected boolean afterChannelInactive( ChannelControlBlock ccb, Object context ) throws ChannelHandleException {
        UlfAsyncMessengerChannelControlBlock cb = (UlfAsyncMessengerChannelControlBlock) ccb;
        Channel channel = cb.getChannel().getNativeHandle();
        Object ob = channel.attr( AttributeKey.valueOf( HuskyCTPConstants.HCTP_DUP_PASSIVE_CHANNEL_KEY ) ).get();
        if ( ob != null && (Boolean)ob ) {
            WolvesAppointClient.this.getLogger().info( "Passive-controlled channel ({}), has detached.", channel.id() );
            UlfClient wrappedClient = WolvesAppointClient.this.getMessageNode();
            if ( wrappedClient.getConnectionArguments().isAutoReconnect() ) {
                try {
                    ArchAsyncMessenger.reconnect( cb, (Messenger) wrappedClient, context );
                    Channel newChannel = cb.getChannel().getNativeHandle();
                    WolvesAppointClient.copyDuplexAttrs( channel, newChannel );

                    UlfInstructMessage instructMessage = new UlfInstructMessage( HuskyCTPConstants.HCTP_DUP_CONTROL_REGISTER );
                    instructMessage.getHead().setIdentityId( wrappedClient.getMessageNodeId() );
                    cb.sendAsynMsg( instructMessage, true );

                    WolvesAppointClient.this.getLogger().info( "Passive-controlled channel ({}, `{}`), reconnect successfully.", channel.id(), cb.getChannel().getAddress() );
                }
                catch ( MediumTerminationException e ) {
                    WolvesAppointClient.this.getLogger().info( "Service already terminated with inactive event. <ACK>" );
                }
                catch ( IOException e ) {
                    WolvesAppointClient.this.getLogger().error( "Passive-controlled channel ({}), attempted to reconnect but failed.", channel.id(), e );
                    throw new ChannelHandleException( e.getCause() );
                }
            }

            DuplexExpress express = (DuplexExpress)WolvesAppointClient.this.mRouteDispatcher.getUMCTExpress();
            express.afterChannelInactive( cb );
            return true; // Blocking next inactive sequence.
        }
        return super.afterChannelInactive( ccb, context );
    }

    private void initSelf() {

    }

    protected WolvesAppointClient( UlfClient messenger, RouteDispatcher dispatcher ) {
        super( messenger, dispatcher.getInterfacialCompiler(), dispatcher.getContextMachinery().getControllerInspector() );
        this.initSelf();
        this.mRouteDispatcher = dispatcher;
        this.mInstructedChannels = new LinkedTreeMap<>();
    }

    public WolvesAppointClient( UlfClient messenger, InterfacialCompiler compiler, ControllerInspector controllerInspector, UMCTExpress express ){
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
            this.mPMCTContextMachinery = new HuskyContextMachinery( new BytecodeIfacCompiler(
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

        this.createPassiveChannel( nLine );
        for ( Map.Entry<ChannelId, ChannelControlBlock > kv : this.mInstructedChannels.entrySet() ) {
            UlfInstructMessage instructMessage = new UlfInstructMessage( HuskyCTPConstants.HCTP_DUP_CONTROL_REGISTER );
            instructMessage.getHead().setIdentityId( this.mMessenger.getMessageNodeId() );

            ChannelControlBlock ccb = kv.getValue();
            UlfAsyncMessengerChannelControlBlock cb = (UlfAsyncMessengerChannelControlBlock) ccb;
            Channel channel = cb.getChannel().getNativeHandle();
            channel.attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_ASYNC_MSG_HANDLE_KEY ) ).set( handler );  // Exclusive handler.
            channel.attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_ASY_EXCLUSIVE_HANDLE_KEY ) ).set( true );
            channel.attr( AttributeKey.valueOf( WolfMCStandardConstants.CB_EXTERNAL_CHANNEL_KEY ) ).set( true );
            channel.attr( AttributeKey.valueOf( HuskyCTPConstants.HCTP_DUP_PASSIVE_CHANNEL_KEY ) ).set( true );
            cb.sendAsynMsg( instructMessage, true );

            this.getLogger().info( "Embracing and registering passive controlled channel ({}).", cb.getChannel().getNativeHandle().id() );
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
