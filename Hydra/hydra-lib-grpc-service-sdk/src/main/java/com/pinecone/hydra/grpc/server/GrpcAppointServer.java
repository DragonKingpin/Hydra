package com.pinecone.hydra.grpc.server;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.framework.system.executum.Processum;
import com.pinecone.hydra.appoints.AppointNodus;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcAppointServer implements AppointNodus {

    protected String name;
    protected long messageNodeId;
    protected Server grpcServer;
    protected GrpcProcess grpcProcess;
    protected Processum parentProcess;
    protected final GrpcServerConfig grpcServerConfig;
    protected ServerBuilder<?> serverBuilder;

    public GrpcAppointServer( String name, long messageNodeId, GrpcServerConfig config, Processum parentProcess ) {
        this.name = name;
        this.messageNodeId = messageNodeId;
        this.grpcServerConfig = config;
        this.parentProcess = parentProcess;

        ServerBuilder<?> builder = ServerBuilder.forPort( config.getPort() );

        if( config.getHandshakeTimeoutMillis() > 0 ) {
            builder.handshakeTimeout( config.getHandshakeTimeoutMillis(), TimeUnit.MILLISECONDS );
        }

        if( config.getKeepAliveTimeSeconds() > 0 ) {
            builder.keepAliveTime( config.getKeepAliveTimeSeconds(), TimeUnit.SECONDS );
        }

        builder.keepAliveTimeout( config.getKeepAliveTimeoutSeconds(), TimeUnit.SECONDS );
        builder.permitKeepAliveWithoutCalls( config.isPermitKeepAliveWithoutCalls() );
        builder.maxInboundMessageSize( config.getMaxInboundMessageSize() );
        builder.maxInboundMetadataSize( config.getMaxInboundMetadataSize() );

        builder.executor( Executors.newCachedThreadPool() );
        this.serverBuilder = builder;
    }

    public GrpcAppointServer( String name, long messageNodeId, GrpcServerConfig config ) {
        this( name, messageNodeId, config, null );
    }

    public GrpcAppointServer( long messageNodeId, GrpcServerConfig config ) {
        this(
                messageNodeId,
                config,
                null
        );
    }

    public GrpcAppointServer( long messageNodeId, GrpcServerConfig config, Processum parentProcess ) {
        this(
                "grpc-server-" + config.getHost() + "-" + config.getPort(),
                messageNodeId,
                config,
                parentProcess
        );
    }

    public GrpcAppointServer( GrpcServerConfig config ) {
        this( config.getPort(), config );
    }

    public GrpcAppointServer( GrpcServerConfig config, Processum parentProcess ) {
        this( config.getPort(), config, parentProcess );
    }


    public ServerBuilder<?> serverBuilder() {
        return this.serverBuilder;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public GrpcServerConfig getConfig() {
        return this.grpcServerConfig;
    }

    @Override
    public void close() {
        if( this.grpcServer != null ) {
            this.grpcServer.shutdownNow();
            this.grpcServer = null;
            this.grpcProcess = null;
        }
    }

    public void shutdown() {
        if( this.grpcServer != null ) {
            this.grpcServer.shutdown();
            this.grpcServer = null;
            this.grpcProcess = null;
        }
    }


    @Override
    public void execute() throws Exception {
        try {
            this.start();
        }
        catch ( ProvokeHandleException e ) {
            if ( e.getCause() instanceof Exception ) {
                throw (Exception) e.getCause();
            }
        }
    }

    public void startGrpcServerOnly() throws IOException {
        this.grpcServer.start();
    }

    public void start( Processum parentProcess ) {
        if ( this.grpcServer == null ) {
            this.grpcServer = this.serverBuilder.build();
        }

        this.grpcProcess = new GrpcProcess( this, parentProcess );
        this.grpcProcess.start();
    }

    public void start() {
        this.start( this.parentProcess );
    }

    public GrpcProcess getProcess() {
        return this.grpcProcess;
    }

    public void awaitTermination() throws InterruptedException {
        this.grpcServer.awaitTermination();
    }

    public void awaitTermination( long t, TimeUnit u ) throws InterruptedException {
        this.grpcServer.awaitTermination( t, u );
    }

    @Override
    public long getMessageNodeId() {
        return this.messageNodeId;
    }

    public boolean isShutdown() {
        return this.grpcServer == null || this.grpcServer.isShutdown();
    }

    public boolean isTerminated() {
        return this.grpcServer == null || this.grpcServer.isTerminated();
    }

}
