package com.pinecone.hydra.service.registry.grpc.client;

import com.pinecone.framework.util.StringUtils;
import com.pinecone.hydra.appoints.AppointNodus;
import com.pinecone.hydra.grpc.client.GrpcAppointClient;
import com.pinecone.hydra.service.registry.client.ArchServiceClient;
import com.pinecone.hydra.service.registry.client.ServiceClient;


import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.grpc.server.cs.ControlMessage;
import com.pinecone.hydra.service.registry.grpc.server.cs.ControlStreamGrpc;
import com.pinecone.hydra.service.registry.grpc.server.iface.ServiceLifecycleImpl;
import com.pinecone.hydra.service.registry.grpc.server.iface.ServiceMetaManipulationIfaceImpl;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.*;
import com.pinecone.hydra.service.registry.grpc.server.meta.*;
import com.pinecone.hydra.service.registry.server.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.server.ServiceMetaManipulationIface;

import io.grpc.stub.StreamObserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class GrpcServiceClient extends ArchServiceClient implements ServiceClient {

    protected final Logger mLogger = LoggerFactory.getLogger(this.getClass());
    protected GrpcAppointClient mGrpcAppointClient;
    private StreamObserver<ControlMessage> controlStream;

    protected ServiceLifecycleGrpc.ServiceLifecycleBlockingStub mLifecycleStub;
    protected ServiceMetaGrpc.ServiceMetaBlockingStub mMetaManipulationStub;

    protected ServiceLifecycleIface mLifecycle;
    protected ServiceMetaManipulationIface mMetaManipulation;


    public GrpcServiceClient( @Nullable GUID serviceId, GrpcAppointClient appointClient, GuidAllocator guidAllocator ) {
        super(serviceId, guidAllocator);
        this.mGrpcAppointClient = appointClient;
    }

    public GrpcServiceClient( GrpcAppointClient appointClient, GuidAllocator guidAllocator ) {
        this(null, appointClient, guidAllocator);
    }



    private void initControlStream() {
        ControlStreamGrpc.ControlStreamStub asyncStub = ControlStreamGrpc.newStub( this.mGrpcAppointClient.getChannel() );

        this.controlStream = asyncStub.connect(
                new StreamObserver<ControlMessage>() {

                    @Override
                    public void onNext(ControlMessage value) {
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onCompleted() {
                    }
                }
        );

        ControlMessage message = ControlMessage.newBuilder()
                        .setClientId( this.getClientId() )
                        .build();

        this.controlStream.onNext(message);
    }

    @Override
    protected void initRPCSubsystem() throws ServiceControlRPCException {

    }

    public long getClientId() {
        return this.mGrpcAppointClient.getClientId();
    }

    @Override
    public void startService() throws ServiceControlRPCException {
        if ( !this.mGrpcAppointClient.isShutdown() ) {
            throw new IllegalStateException("gRPC client already started.");
        }

        try {
            this.mGrpcAppointClient.execute();

            this.mLifecycleStub = ServiceLifecycleGrpc.newBlockingStub( this.mGrpcAppointClient.getChannel() );
            this.mMetaManipulationStub = ServiceMetaGrpc.newBlockingStub( this.mGrpcAppointClient.getChannel() );

            this.mLifecycle = new ServiceLifecycleImpl( this.mLifecycleStub );
            this.mMetaManipulation = new ServiceMetaManipulationIfaceImpl( this.mMetaManipulationStub );

            this.mLogger.info("gRPC initialization successful");
            this.initControlStream();
        }
        catch ( Exception e ) {
            throw new ServiceControlRPCException(e);
        }
    }

    @Override
    public void terminateService() {
        if ( !this.mGrpcAppointClient.isTerminated() ) {
            throw new IllegalStateException( "gRPC client not started." );
        }

        this.deregister();

        try {
            this.mGrpcAppointClient.shutdown( 5, TimeUnit.SECONDS );
        }
        catch ( InterruptedException e ) {
            this.mGrpcAppointClient.shutdownNow();
        }
    }

    @Override
    public GUID registerService(GUID serviceId, GUID deployGuid) throws ClientServiceRegisterException {
        RegisterServiceRequest.Builder builder = RegisterServiceRequest.newBuilder();

        builder.setServiceId(serviceId.toString());
        builder.setClientId( this.getClientId() );

        if (deployGuid != null) {
            builder.setDeployId(deployGuid.toString());
        }

        RegisterServiceRequest request = builder.build();

        try {
            RegisterServiceReply reply = this.mLifecycleStub.registerService(request);
            String instanceId = reply.getInstanceId();

            if ( StringUtils.isNotBlank( instanceId ) ) {
                this.mInstanceId = this.mGuidAllocator.parse(instanceId);

                this.mLogger.info(
                        "Successfully register service : {}, instanceId: {}", serviceId, instanceId
                );
            }

        }
        catch ( Exception e ) {
            this.mLogger.error("Register Service {} failed", serviceId.toString());
            throw new ClientServiceRegisterException(e);
        }

        return this.mInstanceId;
    }

    @Override
    public void deregister() {
        if (this.mInstanceId != null) {
            InstanceIdRequest request =
                    InstanceIdRequest.newBuilder()
                            .setInstanceId(this.mInstanceId.toString())
                            .build();

            this.mLifecycleStub.deregisterServiceByInstanceId(request);
        }
    }

    @Override
    public AppointNodus getAppointNodus() {
        return this.mGrpcAppointClient;
    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return this.mGuidAllocator;
    }



    public ServiceLifecycleGrpc.ServiceLifecycleBlockingStub getLifecycleStub() {
        return this.mLifecycleStub;
    }

    public ServiceMetaGrpc.ServiceMetaBlockingStub getMetaManipulationStub() {
        return this.mMetaManipulationStub;
    }

    public ServiceLifecycleIface getServiceLifecycle() {
        return this.mLifecycle;
    }

    public ServiceMetaManipulationIface getMetaManipulation() {
        return this.mMetaManipulation;
    }
}
