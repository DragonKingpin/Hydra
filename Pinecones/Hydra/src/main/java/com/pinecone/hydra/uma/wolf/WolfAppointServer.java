package com.pinecone.hydra.uma.wolf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;

import com.pinecone.hydra.express.Deliver;
import com.pinecone.hydra.servgram.Servgramium;
import com.pinecone.hydra.uma.AppointServer;
import com.pinecone.hydra.uma.ArchAppointNode;
import com.pinecone.hydra.umc.wolfmc.server.UlfServer;
import com.pinecone.hydra.umct.MessageDeliver;
import com.pinecone.hydra.umct.MessageExpress;
import com.pinecone.hydra.umct.MessageJunction;
import com.pinecone.hydra.umct.UMCTExpress;
import com.pinecone.hydra.umct.WolfMCExpress;
import com.pinecone.hydra.umct.husky.machinery.HuskyRouteDispatcher;
import com.pinecone.hydra.umct.husky.machinery.HuskyRouteDispatcherFabricator;
import com.pinecone.hydra.umct.husky.machinery.RouteDispatcher;
import com.pinecone.hydra.umct.mapping.ControllerInspector;
import com.pinecone.hydra.umct.husky.compiler.CompilerEncoder;
import com.pinecone.hydra.umct.husky.compiler.InterfacialCompiler;

/**
 *  Pinecone Ursus For Java WolfAppointServer [ Ulfhedinn Wolf RPC Server ]
 *  Bean Nuts Walnut Ulfhedinn Wolves/Ulfar Family.
 *  Author: Harold.E / JH.W (DragonKing)
 *  Copyright © 2008 - 2028 Bean Nuts Foundation All rights reserved.
 *  *****************************************************************************************
 */
public class WolfAppointServer extends ArchAppointNode implements AppointServer {
    protected UlfServer                     mRecipient;
    protected RouteDispatcher               mRouteDispatcher;

    protected void applyExpress( UMCTExpress express ) {
        this.mRecipient.apply( express );
    }

    protected WolfAppointServer( UlfServer messenger, RouteDispatcher dispatcher ){
        super( (Servgramium) messenger, dispatcher.getContextMachinery() );
        this.mRecipient       = messenger;
        this.mRouteDispatcher = dispatcher;
    }

    public WolfAppointServer( UlfServer messenger, InterfacialCompiler compiler, ControllerInspector controllerInspector, UMCTExpress express ){
        this( messenger, new HuskyRouteDispatcher( compiler, controllerInspector, express ) );
        this.apply( express );
    }

    public WolfAppointServer( UlfServer messenger, CompilerEncoder encoder, UMCTExpress express ){
        this( messenger, new HuskyRouteDispatcher( encoder, express, messenger.getTaskManager().getClassLoader() ) );
        this.apply( express );
    }

    public WolfAppointServer( UlfServer messenger, UMCTExpress express ){
        this( messenger, new HuskyRouteDispatcher( express, messenger.getTaskManager().getClassLoader() ) );
        this.apply( express );
    }

    public WolfAppointServer( UlfServer messenger, Class<?> expressType ){
        this( messenger, new HuskyRouteDispatcher( messenger.getTaskManager().getClassLoader(), true ) );

        try{
            Constructor<?> constructor = expressType.getConstructor( String.class, MessageJunction.class, Logger.class );
            UMCTExpress express = (UMCTExpress) constructor.newInstance(DefaultEntityName, this, this.getLogger() );

            this.applyExpress( express );
            HuskyRouteDispatcherFabricator.afterConstructed( (HuskyRouteDispatcher)this.mRouteDispatcher, express );
        }
        catch ( NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e ) {
            throw new IllegalArgumentException( "`" + expressType.getSimpleName() + "` is not UMCTExpress calibre qualified." );
        }
    }

    public WolfAppointServer( UlfServer messenger ){
        this( messenger, WolfMCExpress.class );
    }




    @Override
    public void close() {
        this.mRecipient.close();
    }

    @Override
    public UlfServer getMessageNode() {
        return this.mRecipient;
    }

    @Override
    public WolfAppointServer apply( UMCTExpress handler ) {
        this.mRouteDispatcher.setUMCTExpress( handler );
        this.mRecipient.apply( handler );
        return this;
    }

    @Override
    public UMCTExpress getUMCTExpress() {
        return this.mRouteDispatcher.getUMCTExpress();
    }

    @Override
    public MessageExpress register( Deliver deliver ) {
        return this.mRouteDispatcher.register( deliver );
    }

    @Override
    public MessageExpress  fired   ( Deliver deliver ) {
        return this.mRouteDispatcher.fired( deliver );
    }

    @Override
    public MessageDeliver getDeliver( String name ) {
        return this.mRouteDispatcher.getDeliver( name );
    }

    @Override
    public MessageDeliver getDefaultDeliver() {
        return this.mRouteDispatcher.getDefaultDeliver();
    }

    @Override
    public void registerInstance( String deliverName, Object instance, Class<?> iface ) {
        this.mRouteDispatcher.registerInstance( deliverName, instance, iface );
    }

    @Override
    public void registerInstance( Object instance, Class<?> iface ) {
        this.mRouteDispatcher.registerInstance( instance, iface );
    }

    @Override
    public void registerController( String deliverName, Object instance, Class<?> controllerType ) {
        this.mRouteDispatcher.registerController( deliverName, instance, controllerType );
    }

    @Override
    public void registerController( Object instance, Class<?> controllerType ) {
        this.mRouteDispatcher.registerController( instance, controllerType );
    }
}
