package com.pinecone.hydra.service.registry.grpc.server;

import java.util.concurrent.TimeUnit;

import com.pinecone.hydra.grpc.server.GrpcAppointServer;
import com.pinecone.hydra.grpc.server.GrpcProcess;
import com.pinecone.hydra.grpc.server.GrpcServerConfig;
import com.pinecone.hydra.service.registry.appoint.ServiceAppointServer;
import com.pinecone.hydra.service.registry.server.ServiceManager;

import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;

public class GrpcServiceAppointServer implements ServiceAppointServer {

    protected ServiceManager serviceManager;

    protected final GrpcAppointServer grpcAppointServer;;

    public GrpcServiceAppointServer( GrpcAppointServer server ) {
        this.grpcAppointServer = server;
    }

    @Override
    public ServiceManager serviceManager() {
        return this.serviceManager;
    }

    @Override
    public ServiceAppointServer hookServiceManager( ServiceManager serviceManager ) {
        if (this.serviceManager != null) {
            throw new IllegalStateException("Manager has already hooked.");
        }

        this.serviceManager = serviceManager;

        ServerBuilder<?> build = this.grpcAppointServer.serverBuilder();
        build
                .addService(new GrpcServiceLifecycleService(serviceManager))
                .addService(new GrpcServiceMetaService(serviceManager))
                .addService(ServerInterceptors.intercept(
                        new GrpcControlStreamService(serviceManager, this),
                        new ClientMetaDataInterceptor()
                ))
        ;

        this.serviceManager.getLogger().info( "GrpcAppointServer[{}] has been hooked.", this.getName() );
        return this;
    }

    @Override
    public String getName() {
        return this.grpcAppointServer.getName();
    }

    @Override
    public GrpcServerConfig getConfig() {
        return this.grpcAppointServer.getConfig();
    }

    @Override
    public void close() {
        this.grpcAppointServer.close();
    }

    public void awaitTermination() throws InterruptedException {
        this.grpcAppointServer.awaitTermination();
    }

    public void awaitTermination( long t, TimeUnit u ) throws InterruptedException {
        this.grpcAppointServer.awaitTermination( t, u );
    }

    public GrpcProcess getProcess() {
        return this.grpcAppointServer.getProcess();
    }

    @Override
    public void execute() throws Exception {
        this.grpcAppointServer.execute();
    }

    @Override
    public long getMessageNodeId() {
        return this.grpcAppointServer.getMessageNodeId();
    }

    @Override
    public boolean isTerminated() {
        return this.grpcAppointServer == null || this.grpcAppointServer.isTerminated();
    }

    @Override
    public boolean isStarted() {
        return !this.grpcAppointServer.isShutdown();
    }
}
