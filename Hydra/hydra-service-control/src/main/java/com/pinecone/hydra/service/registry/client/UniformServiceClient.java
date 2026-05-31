package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.appoints.AppointNodus;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.client.instruction.ServiceClientDeregisterInstruction;
import com.pinecone.hydra.service.registry.client.instruction.ServiceClientRegisterInstruction;
import com.pinecone.hydra.service.registry.client.control.ServiceClientManipulationHandler;
import com.pinecone.hydra.service.registry.client.entity.ServiceClientRegisterResult;
import com.pinecone.hydra.service.registry.client.port.ServiceLifecyclePort;
import com.pinecone.hydra.service.registry.client.port.ServiceMetaPort;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransport;
import com.pinecone.hydra.service.registry.client.transport.ServiceClientTransportException;

public class UniformServiceClient extends ArchServiceClient {

    protected ServiceClientTransport mTransport;

    protected ServiceLifecyclePort mLifecyclePort;

    protected ServiceMetaPort mMetaPort;

    protected volatile boolean mbTerminated = false;

    public UniformServiceClient(
            @Nullable GUID serviceId,
            GuidAllocator guidAllocator,
            ServiceClientTransport transport
    ) {
        super( serviceId, guidAllocator );
        this.mTransport = transport;
    }

    public UniformServiceClient( GuidAllocator guidAllocator, ServiceClientTransport transport ) {
        this( null, guidAllocator, transport );
    }

    @Override
    protected void initRPCSubsystem() throws ServiceControlRPCException {
        try {
            if ( !this.mTransport.isReady() ) {
                this.mTransport.connect();
            }
            this.mLifecyclePort = this.mTransport.getPort( ServiceLifecyclePort.class );
            this.mMetaPort = this.mTransport.getPort( ServiceMetaPort.class );
            this.mbTerminated = false;
        }
        catch ( ServiceClientTransportException e ) {
            throw new ServiceControlRPCException( e );
        }
    }

    @Override
    public void terminateService() {
        if ( this.mbTerminated ) {
            return;
        }
        this.mbTerminated = true;

        try {
            this.deregister();
        }
        finally {
            this.mTransport.disconnect();
        }
    }

    @Override
    public long getClientId() {
        return this.mTransport.getClientId();
    }

    @Override
    public AppointNodus getAppointNodus() {
        return null;
    }

    @Override
    public GUID registerService( GUID serviceId, GUID deployGuid ) throws ClientServiceRegisterException {
        ServiceClientRegisterInstruction command = new ServiceClientRegisterInstruction();
        command.setServiceGuid( serviceId );
        command.setDeployGuid( deployGuid );
        command.setClientId( this.mTransport.getClientId() );

        try {
            ServiceClientRegisterResult result = this.lifecycle().register( command );
            this.mServiceId = result.getServiceGuid();
            this.mInstanceId = result.getInstanceGuid();
            return this.mInstanceId;
        }
        catch ( ServiceClientTransportException e ) {
            throw new ClientServiceRegisterException( e );
        }
    }

    @Override
    public void deregister() {
        if ( this.mInstanceId == null ) {
            return;
        }

        ServiceClientDeregisterInstruction command = new ServiceClientDeregisterInstruction();
        command.setInstanceGuid( this.mInstanceId );
        try {
            this.lifecycle().deregister( command );
            this.mInstanceId = null;
        }
        catch ( ServiceClientTransportException e ) {
            throw new IllegalStateException( e );
        }
    }

    @Override
    public ServiceLifecyclePort lifecycle() {
        if ( this.mLifecyclePort == null ) {
            throw new IllegalStateException( "Service lifecycle port has not initialized." );
        }
        return this.mLifecyclePort;
    }

    @Override
    public ServiceMetaPort meta() {
        if ( this.mMetaPort == null ) {
            throw new IllegalStateException( "Service meta port has not initialized." );
        }
        return this.mMetaPort;
    }

    @Override
    public void registerStateSynchronizedHandler( ServiceClientStateSynchronizedHandler handler ) {
        this.mTransport.registerStateSynchronizedHandler( handler );
    }

    @Override
    public void deregisterStateSynchronizedHandler( ServiceClientStateSynchronizedHandler handler ) {
        this.mTransport.deregisterStateSynchronizedHandler( handler );
    }

    @Override
    public void registerManipulationHandler( ServiceClientManipulationHandler handler ) {
        this.mTransport.registerManipulationHandler( handler );
    }

    @Override
    public void deregisterManipulationHandler( ServiceClientManipulationHandler handler ) {
        this.mTransport.deregisterManipulationHandler( handler );
    }

}
