package com.pinecone.hydra.service.registry.client;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.constant.ServiceStatus;
import com.pinecone.hydra.service.registry.dao.ServiceInstanceDO;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.service.registry.exception.ClientRegisterServiceException;
import com.pinecone.hydra.service.registry.exception.CreateServiceInstanceException;
import com.pinecone.hydra.service.registry.exception.RpcInitException;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointClient;
import com.pinecone.hydra.umc.wolf.client.UlfClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class UniformServiceManagerClient implements ServiceManagerClient {
    protected UniformServiceInstrument      mServiceInstrument;

    protected DuplexAppointClient           mDuplexAppointClient;

    protected Logger                        mLogger;

    protected UlfClient                     mRPCClient;

    protected ServiceLifecycleIface         mServiceLifecycleIface;

    protected GuidAllocator                 mGuidAllocator;

    protected String                        mIp;

    public UniformServiceManagerClient( UniformServiceInstrument serviceInstrument, UlfClient ulfClient, GuidAllocator guidAllocator, String ip ) {
        this.mServiceInstrument     = serviceInstrument;
        this.mLogger                = LoggerFactory.getLogger( this.getClass() );
        this.mRPCClient             = ulfClient;
        this.mGuidAllocator         = guidAllocator;
        this.mIp                    = ip;
    }

    @Override
    public void startService() throws RpcInitException {
        this.initRPCSubsystem();
    }

    @Override
    public void terminateService() {
        if( this.mDuplexAppointClient == null ) {
            throw new IllegalStateException( "RPCClient dose not started yet." );
        }

        this.mDuplexAppointClient.terminate();
        this.mDuplexAppointClient = null;
    }

    protected void initRPCSubsystem() throws RpcInitException {
        if ( this.mDuplexAppointClient != null && !this.mDuplexAppointClient.getMessageNode().isTerminated() ) {
            throw new IllegalStateException( "DuplexAppointClient has started." );
        }

        this.mDuplexAppointClient = new WolvesAppointClient( this.mRPCClient );

        try {
            this.mDuplexAppointClient.compile( ServiceLifecycleIface.class, false );
            this.mServiceLifecycleIface = this.mDuplexAppointClient.getIface( ServiceLifecycleIface.class );
            this.mLogger.info( "RPC initialization successful" );
        } catch ( Exception e ) {
            this.mServiceLifecycleIface = null;
            throw new RpcInitException( e );
        }
    }

    @Override
    public GUID registerService( GUID serviceId, GUID deployGuid ) throws CreateServiceInstanceException, ClientRegisterServiceException {
        this.createServiceInstance( serviceId, deployGuid );
        RegisterServiceDTO serviceDTO = new RegisterServiceDTO();
        serviceDTO.setServiceId( serviceId.toString() );
        serviceDTO.setClientId( this.mRPCClient.getMessageNodeId() );
        try {
            this.mServiceLifecycleIface.registerService( serviceDTO );
        }catch (Exception e) {
            this.mLogger.info( "Register Service {} failed", serviceDTO.getServiceId() );
            throw new ClientRegisterServiceException( e );
        }
        return null;
    }

    protected GUID createServiceInstance( GUID serviceId, GUID deployGuid ) throws CreateServiceInstanceException {
        GUID guid = this.mGuidAllocator.nextGUID();
        ServiceInstanceDO instanceDO = new ServiceInstanceDO();

        instanceDO.setDeployGuid( deployGuid );
        instanceDO.setStatus( ServiceStatus.SERVICE_NEW.getCode() );
        instanceDO.setLatestStartTime( LocalDateTime.now() );
        instanceDO.setIp( this.mIp );
        instanceDO.setGuid( guid );
        instanceDO.setServiceGuid( serviceId );
        try {
            this.mServiceInstrument.createServiceInstance( instanceDO );
            this.mLogger.info( "ServiceInstance create successfully" );
        }catch (Exception e) {
            throw new CreateServiceInstanceException( e );
        }

        return guid;
    }
}
