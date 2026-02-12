package com.pinecone.hydra.service.registry.grpc.server;

import com.pinecone.framework.system.ProvokeHandleException;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyReply;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceLifecycleGrpc;
import com.pinecone.hydra.service.registry.server.ServiceLifecycleService;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import io.grpc.stub.StreamObserver;

public class GrpcServiceLifecycleService extends ServiceLifecycleGrpc.ServiceLifecycleImplBase {

    private final ServiceLifecycleService lifecycleService;

    public GrpcServiceLifecycleService(ServiceManager serviceManager) {
        this.lifecycleService = serviceManager.serviceLifecycleService();
    }

    @Override
    public void registerService( RegisterServiceRequest request, StreamObserver<RegisterServiceReply> responseObserver ) {
        try {
            RegisterServiceDTO dto = new RegisterServiceDTO();
            dto.setClientId(request.getClientId());
            dto.setServiceId(request.getServiceId());
            dto.setDeployId(request.getDeployId());

            String instanceId = this.lifecycleService.registerService(dto);

            RegisterServiceReply.Builder builder = RegisterServiceReply.newBuilder();

            if (instanceId != null) {
                builder.setInstanceId(instanceId);
            } else {
                builder.setInstanceId("");
            }

            RegisterServiceReply reply = builder.build();

            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
        catch ( ClientServiceRegisterException e ) {
            throw new ProvokeHandleException( e );
        }
    }

    @Override
    public void deregisterServiceByClientId(ClientIdRequest request, StreamObserver<EmptyReply> responseObserver) {
        this.lifecycleService.deregisterServiceByClientId(request.getClientId());

        EmptyReply reply = EmptyReply.newBuilder().build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void deregisterServiceByInstanceId(InstanceIdRequest request, StreamObserver<EmptyReply> responseObserver) {

        this.lifecycleService.deregisterServiceByInstanceId(request.getInstanceId());

        EmptyReply reply = EmptyReply.newBuilder().build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void hasOwnedServiceByServiceId(ServiceIdRequest request, StreamObserver<BoolReply> responseObserver) {
        boolean result = this.lifecycleService.hasOwnedServiceByServiceId(
                request.getServiceId()
        );

        BoolReply reply = BoolReply.newBuilder()
                .setValue(result)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void hasOwnedServiceInstanceByClientId(ClientIdRequest request, StreamObserver<BoolReply> responseObserver) {
        boolean result = this.lifecycleService.hasOwnedServiceInstance(
                request.getClientId()
        );

        BoolReply reply = BoolReply.newBuilder()
                .setValue(result)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void hasOwnedServiceInstanceByInstanceId(InstanceIdRequest request, StreamObserver<BoolReply> responseObserver) {
        boolean result = this.lifecycleService.hasOwnedServiceInstance(
                request.getInstanceId()
        );

        BoolReply reply = BoolReply.newBuilder()
                .setValue(result)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void hasOwnedServiceClient(ClientIdRequest request, StreamObserver<BoolReply> responseObserver) {
        boolean result = this.lifecycleService.hasOwnedServiceClient(
                request.getClientId()
        );

        BoolReply reply = BoolReply.newBuilder()
                .setValue(result)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    @Override
    public void countRegisteredService(EmptyRequest request, StreamObserver<CountReply> responseObserver) {

        Integer count = this.lifecycleService.countRegisteredService();

        CountReply.Builder builder = CountReply.newBuilder();

        if (count != null) {
            builder.setValue(count);
        } else {
            builder.setValue(0);
        }

        CountReply reply = builder.build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
