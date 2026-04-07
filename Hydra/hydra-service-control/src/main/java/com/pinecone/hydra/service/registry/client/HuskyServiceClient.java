package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.system.Nullable;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.registry.server.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.server.ServiceMetaManipulationIface;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HuskyServiceClient extends ArchServiceClient implements ServiceClient {
    protected DuplexAppointClient           mDuplexAppointClient;

    protected UlfClient                     mRPCClient;

    protected ServiceLifecycleIface         mServiceLifecycleIface;

    protected ServiceMetaManipulationIface  mServiceMetaManipulationIface;

    public HuskyServiceClient( @Nullable GUID serviceId, UlfClient ulfClient, GuidAllocator guidAllocator ) {
        super( serviceId, guidAllocator );
        this.mRPCClient             = ulfClient;
    }

    public HuskyServiceClient( UlfClient ulfClient, GuidAllocator guidAllocator ) {
        this( null, ulfClient, guidAllocator );
    }

    @Override
    public void startService() throws ServiceControlRPCException {
        this.initRPCSubsystem();
    }

    @Override
    public void terminateService() {
        if( this.mDuplexAppointClient == null ) {
            throw new IllegalStateException( "RPCClient dose not started yet." );
        }

        this.deregister();
        this.mDuplexAppointClient.terminate();
        this.mDuplexAppointClient = null;
    }

    @Override
    public DuplexAppointClient getAppointNodus() {
        return this.mDuplexAppointClient;
    }

    @Override
    public GuidAllocator getGuidAllocator() {
        return this.mGuidAllocator;
    }

    protected void initRPCSubsystem() throws ServiceControlRPCException {
        if ( this.mDuplexAppointClient != null && !this.mDuplexAppointClient.getMessageNode().isTerminated() ) {
            throw new IllegalStateException( "DuplexAppointClient has started." );
        }

        this.mDuplexAppointClient = new WolvesAppointClient( this.mRPCClient );

        try {
            this.mDuplexAppointClient.execute();
            this.mDuplexAppointClient.compile( ServiceLifecycleIface.class, false );
            this.mDuplexAppointClient.compile( ServiceMetaManipulationIface.class, false );
            this.mServiceLifecycleIface = this.mDuplexAppointClient.getIface( ServiceLifecycleIface.class );
            this.mServiceMetaManipulationIface = this.mDuplexAppointClient.getIface( ServiceMetaManipulationIface.class );
            this.mLogger.info( "RPC initialization successful" );
        }
        catch ( Exception e ) {
            this.mServiceLifecycleIface = null;
            throw new ServiceControlRPCException( e );
        }
    }

    @Override
    public GUID registerService( GUID serviceId, GUID deployGuid ) throws ClientServiceRegisterException {
        RegisterServiceDTO serviceDTO = new RegisterServiceDTO();
        serviceDTO.setServiceId( serviceId.toString() );
        serviceDTO.setClientId( this.mRPCClient.getMessageNodeId() );
        if ( deployGuid != null ) {
            serviceDTO.setDeployId( deployGuid.toString() );
        }
        this.mServiceId = serviceId;

        try {
            String insId = this.mServiceLifecycleIface.registerService( serviceDTO );
            if ( insId != null ) {
                this.mInstanceId = this.mGuidAllocator.parse( insId );
                this.mLogger.info( "Successfully register service : {}, instanceId: {}", serviceDTO.getServiceId(), insId );
            }
        }
        catch ( Exception e ) {
            this.mLogger.error( "Register Service {} failed", serviceDTO.getServiceId() );
            throw new ClientServiceRegisterException( e );
        }
        return this.mInstanceId;
    }

    @Override
    public void deregister() {
        if ( this.mInstanceId != null ) {
            this.mServiceLifecycleIface.deregisterServiceByInstanceId( this.mInstanceId.toString() );
        }
    }
}
