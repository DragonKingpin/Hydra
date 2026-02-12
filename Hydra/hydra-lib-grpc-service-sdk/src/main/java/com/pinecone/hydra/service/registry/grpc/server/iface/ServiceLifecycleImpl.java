package com.pinecone.hydra.service.registry.grpc.server.iface;

import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.BoolReply;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.ClientIdRequest;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.CountReply;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.EmptyRequest;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.InstanceIdRequest;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceReply;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.RegisterServiceRequest;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceIdRequest;
import com.pinecone.hydra.service.registry.grpc.server.lifecycle.ServiceLifecycleGrpc;
import com.pinecone.hydra.service.registry.server.ServiceLifecycleIface;

public class ServiceLifecycleImpl implements ServiceLifecycleIface {

    protected final ServiceLifecycleGrpc.ServiceLifecycleBlockingStub lifecycleBlockingStub;

    public ServiceLifecycleImpl( ServiceLifecycleGrpc.ServiceLifecycleBlockingStub lifecycleBlockingStub ) {
        this.lifecycleBlockingStub = lifecycleBlockingStub;
    }

    @Override
    public String registerService( RegisterServiceDTO serviceDTO ) {
        RegisterServiceRequest request =
                RegisterServiceRequest.newBuilder()
                        .setClientId( serviceDTO.getClientId() )
                        .setServiceId( serviceDTO.getServiceId() )
                        .setDeployId( serviceDTO.getDeployId() )
                        .build();
        RegisterServiceReply reply = this.lifecycleBlockingStub.registerService( request );
        return reply.getInstanceId();
    }

    @Override
    public boolean createInstanceMeta(ServiceInstanceEntry serviceInstanceEntry) {
        return false;
    }

    @Override
    public void deregisterServiceByClientId( Long clientId ) {
        ClientIdRequest request = ClientIdRequest.newBuilder().setClientId( clientId ).build();
        this.lifecycleBlockingStub.deregisterServiceByClientId( request );
    }


    @Override
    public void deregisterServiceByInstanceId( String instanceId ) {
        InstanceIdRequest request = InstanceIdRequest.newBuilder().setInstanceId( instanceId ).build();
        this.lifecycleBlockingStub.deregisterServiceByInstanceId( request );
    }


    @Override
    public boolean hasOwnedServiceByServiceId( String serviceId ) {
        ServiceIdRequest request = ServiceIdRequest.newBuilder().setServiceId( serviceId ).build();
        BoolReply reply = this.lifecycleBlockingStub.hasOwnedServiceByServiceId( request );

        return reply.getValue();
    }


    @Override
    public boolean hasOwnedServiceInstance( Long clientId ) {
        ClientIdRequest request = ClientIdRequest.newBuilder().setClientId( clientId ).build();
        BoolReply reply = this.lifecycleBlockingStub.hasOwnedServiceInstanceByClientId( request );
        return reply.getValue();
    }


    @Override
    public boolean hasOwnedServiceInstance( String instanceId ) {
        InstanceIdRequest request = InstanceIdRequest.newBuilder().setInstanceId( instanceId ).build();
        BoolReply reply = this.lifecycleBlockingStub.hasOwnedServiceInstanceByInstanceId( request );
        return reply.getValue();
    }


    @Override
    public boolean hasOwnedServiceClient( Long clientId ) {
        ClientIdRequest request = ClientIdRequest.newBuilder().setClientId( clientId ).build();
        BoolReply reply = this.lifecycleBlockingStub.hasOwnedServiceClient( request );
        return reply.getValue();
    }


    @Override
    public Integer countRegisteredService() {
        EmptyRequest request = EmptyRequest.newBuilder().build();
        CountReply reply = this.lifecycleBlockingStub.countRegisteredService( request );
        return reply.getValue();
    }
}
