package com.pinecone.hydra.grpc.client;

import java.util.concurrent.TimeUnit;

import com.pinecone.hydra.appoints.AppointNodus;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcAppointClient implements AppointNodus {

    protected String name;
    protected long messageNodeId;

    protected ManagedChannel managedChannel;
    protected final GrpcClientConfig grpcClientConfig;

    protected ManagedChannelBuilder<?> channelBuilder;


    public GrpcAppointClient( String name, long messageNodeId, GrpcClientConfig config ) {
        this.name = name;
        this.messageNodeId = messageNodeId;
        this.grpcClientConfig = config;

        ManagedChannelBuilder<?> builder = ManagedChannelBuilder
                        .forAddress( config.getHost(), config.getPort() )
                        .usePlaintext();

        if( config.getIdleTimeoutMillis() > 0 ) {
            builder.idleTimeout( config.getIdleTimeoutMillis(), TimeUnit.MILLISECONDS );
        }

        if( config.getKeepAliveTimeSeconds() > 0 ) {
            builder.keepAliveTime( config.getKeepAliveTimeSeconds(), TimeUnit.SECONDS );
            builder.keepAliveWithoutCalls( true );
        }

        this.channelBuilder = builder;
    }


    public GrpcAppointClient( long messageNodeId, GrpcClientConfig config ) {
        this(
                "grpc-client-" + config.getHost() + "-" + config.getPort(),
                messageNodeId,
                config
        );
    }


    public GrpcAppointClient( GrpcClientConfig config ) {
        this( config.getPort(), config );
    }


    public ManagedChannelBuilder<?> channelBuilder() {
        return this.channelBuilder;
    }


    public ManagedChannel getChannel() {
        return this.managedChannel;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public GrpcClientConfig getConfig() {
        return this.grpcClientConfig;
    }

    @Override
    public void close() {
        if( this.managedChannel != null ) {
            this.managedChannel.shutdownNow();
            this.managedChannel = null;
        }
    }

    public void shutdown( long t, TimeUnit u ) throws InterruptedException {
        if( this.managedChannel != null ) {
            this.managedChannel.shutdown().awaitTermination( t, u );
            this.managedChannel = null;
        }
    }

    public void shutdownNow() {
        if( this.managedChannel != null ) {
            this.managedChannel.shutdownNow();
            this.managedChannel = null;
        }
    }

    @Override
    public void execute() throws Exception {
        if( this.managedChannel == null ) {
            this.managedChannel = this.channelBuilder.build();
        }
    }

    @Override
    public long getMessageNodeId() {
        return this.messageNodeId;
    }

    public long getClientId() {
        return this.getMessageNodeId();
    }

    public boolean isShutdown() {
        return this.managedChannel == null || this.managedChannel.isShutdown();
    }

    public boolean isTerminated() {
        return this.managedChannel == null || this.managedChannel.isTerminated();
    }
}
