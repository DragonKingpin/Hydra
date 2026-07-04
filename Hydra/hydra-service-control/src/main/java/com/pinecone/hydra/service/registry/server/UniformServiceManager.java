package com.pinecone.hydra.service.registry.server;

import com.mysql.cj.exceptions.AssertionFailedException;
import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.kom.entity.GenericServiceInstanceEntity;
import com.pinecone.hydra.service.kom.entity.ServiceElement;
import com.pinecone.hydra.service.kom.entity.ServiceInstanceEntry;
import com.pinecone.hydra.service.registry.ClientServiceRegisterException;
import com.pinecone.hydra.service.registry.dto.RegisterServiceDTO;
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.UniformService;
import com.pinecone.hydra.service.registry.WolfServiceInstance;
import com.pinecone.hydra.service.registry.appoint.ServiceClientile;
import com.pinecone.hydra.service.registry.appoint.ServiceAppointServer;
import com.pinecone.hydra.service.registry.constant.ServiceInstanceStatus;
import com.pinecone.hydra.service.registry.event.InstanceLifecycleEvent;
import com.pinecone.hydra.service.registry.event.InstanceLifecycleEventHandler;
import com.pinecone.hydra.service.registry.server.detached.ServiceDetachedObservationConfig;
import com.pinecone.hydra.service.registry.server.detached.ServiceDetachedObservationEntry;
import com.pinecone.hydra.service.registry.server.detached.ServiceDetachedObservationRegistry;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransport;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransportRegistry;
import com.pinecone.hydra.service.registry.server.transport.ServiceControlTransportType;
import com.pinecone.hydra.service.registry.server.transport.UniformServiceControlTransportRegistry;
import com.pinecone.hydra.service.registry.server.inspection.ServiceControlInspection;
import com.pinecone.hydra.service.registry.server.inspection.ServiceTransportInspection;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UniformServiceManager implements ServiceManager {
    protected final ServiceInstrument                                                       mServiceInstrument;

    protected final ConcurrentMap<Long, ServiceAppointServer >                              mServerPoolMap;  // ServerId => Node
    protected final ConcurrentMap<Long, ServiceInstance >                                   mCIdInstanceRegistry; // ClientId => Instance
    protected final ConcurrentMap<Identification, ConcurrentMap<Long, ServiceInstance> >    mServiceRegistry;  // ServiceId => <CId, Instance>
    protected final ConcurrentMap<Identification, ClientInstance >                          mInstanceRegistry; // InstanceId => Instance
    protected final ConcurrentMap<Long, ServiceClientile>                                   mClientRegistry; // ClientId => Client

    protected final List<InstanceLifecycleEventHandler>                                     mRegisterEventHandlers;
    protected final GuidAllocator                                                           mGuidAllocator;
    protected final ServiceEventHooker                                                      mServiceEventHooker;
    protected final ServiceControlTransportRegistry                                         mTransportRegistry;

    protected final ServiceLifecycleService                                                 mServiceLifecycleService;
    protected final ServiceMetaService                                                      mServiceMetaService;
    protected final ServiceDetachedObservationRegistry                                      mDetachedObservationRegistry;
    protected ServiceDetachedObservationConfig                                              mDetachedObservationConfig;
    protected ScheduledExecutorService                                                      mDetachedObservationSweeper;
    protected ExecutorService                                                               mDetachedExpirationExecutor;
    protected final AtomicLong                                                              mDetachedObservationSequence;

    private final Logger mLogger;

    private final ReadWriteLock mEventHandlerLock = new ReentrantReadWriteLock();

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }


    protected void vitalizeRPCSubsystem() throws ServiceControlRPCException {
        try {
            for ( Map.Entry<Long, ServiceAppointServer> entry : this.mServerPoolMap.entrySet() ) {
                if ( !entry.getValue().isStarted() ) {
                    entry.getValue().execute();
                }
            }
            this.infoLifecycle( "RPC Subsystem Service Vitalization", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            throw new ServiceControlRPCException( e );
        }
    }

    public UniformServiceManager( ServiceInstrument serviceInstrument ) {
        this.mServiceInstrument         = serviceInstrument;
        this.mServerPoolMap             = new ConcurrentHashMap<>();
        this.mServiceRegistry           = new ConcurrentHashMap<>();
        this.mCIdInstanceRegistry       = new ConcurrentHashMap<>();
        this.mInstanceRegistry          = new ConcurrentHashMap<>();
        this.mClientRegistry            = new ConcurrentHashMap<>();
        this.mLogger                    = LoggerFactory.getLogger( this.getClass() );
        this.mRegisterEventHandlers     = new ArrayList<>();
        this.mGuidAllocator             = serviceInstrument.getGuidAllocator();
        this.mServiceEventHooker        = new UniformServiceEventHooker( this );
        this.mTransportRegistry         = new UniformServiceControlTransportRegistry();
        this.mDetachedObservationConfig = new ServiceDetachedObservationConfig();
        this.mDetachedObservationRegistry = new ServiceDetachedObservationRegistry();
        this.mDetachedObservationSequence = new AtomicLong();


        this.mServiceLifecycleService   = new ServiceLifecycleService( this );
        this.mServiceMetaService        = new ServiceMetaService( this );

    }

    @Override
    public Collection<ServiceAppointServer> getServers() {
        return this.mServerPoolMap.values();
    }

    @Override
    public ServiceManager addAppointServer( ServiceAppointServer appointServer ) {
        this.mServerPoolMap.put( appointServer.getMessageNodeId(), appointServer );
        return this;
    }

    @Override
    public ServiceManager hookAppointServer( ServiceAppointServer appointServer ) {
        this.addAppointServer( appointServer );
        appointServer.hookServiceManager( this );
        return this;
    }

    @Override
    public ServiceManager vitalizeAppointServer( ServiceAppointServer appointServer ) throws ServiceControlRPCException {
        try {
            this.hookAppointServer( appointServer );
            appointServer.execute();
            return this;
        }
        catch ( Exception e ) {
            throw new ServiceControlRPCException( e );
        }
    }

    @Override
    public ServiceAppointServer getAppointServerById( Long appointNodeId ) {
        return this.mServerPoolMap.get( appointNodeId );
    }

    @Override
    public ServiceAppointServer evictAppointServerById( Long appointNodeId ) {
        ServiceAppointServer legacy = this.mServerPoolMap.remove( appointNodeId );
        if ( legacy != null ) {
            legacy.close(); // In principle, all connections will be closed cascadingly.
            return legacy;
        }
        return null;
    }

    @Override
    public int serverSize() {
        return this.mServerPoolMap.size();
    }

    @Override
    public ServiceEventHooker serviceEventHooker() {
        return this.mServiceEventHooker;
    }

    @Override
    public ServiceControlTransportRegistry transportRegistry() {
        return this.mTransportRegistry;
    }

    @Override
    public void configureDetachedObservation( ServiceDetachedObservationConfig config ) {
        if ( config == null ) {
            this.mDetachedObservationConfig = new ServiceDetachedObservationConfig();
            return;
        }
        this.mDetachedObservationConfig = config;
    }




    @Override
    public ServiceLifecycleService serviceLifecycleService() {
        return this.mServiceLifecycleService;
    }

    @Override
    public ServiceMetaService getServiceMetaService() {
        return this.mServiceMetaService;
    }

    @Override
    public ServiceControlInspection inspectServiceControl() {
        ServiceControlInspection status = new ServiceControlInspection();
        List<ServiceTransportInspection> transportStatuses = new ArrayList<>();
        boolean bAnyTransportStarted = false;
        boolean bAllTransportStarted = !this.mTransportRegistry.transports().isEmpty();
        int nConnectedClientCount = 0;

        for ( ServiceControlTransport transport : this.mTransportRegistry.transports() ) {
            ServiceTransportInspection transportStatus = this.inspectTransport( transport );
            transportStatuses.add( transportStatus );
            if ( transportStatus.isStarted() ) {
                bAnyTransportStarted = true;
            }
            if ( !transportStatus.isStarted() ) {
                bAllTransportStarted = false;
            }
            nConnectedClientCount += transportStatus.getConnectedClientCount();
        }

        status.setServiceInstrumentReady( this.mServiceInstrument != null );
        status.setServiceManagerStarted( bAllTransportStarted );
        status.setRegisteredServiceCount( this.mServiceRegistry.size() );
        status.setRuntimeInstanceCount( this.mInstanceRegistry.size() );
        status.setConnectedClientCount( nConnectedClientCount );
        status.setTransports( transportStatuses );
        status.setInstanceStatusCounts( this.queryInstanceStatusCounts() );
        status.setOverallStatus( this.resolveOverallStatus( status, bAnyTransportStarted, bAllTransportStarted ) );
        return status;
    }

    protected ServiceTransportInspection inspectTransport( ServiceControlTransport transport ) {
        ServiceTransportInspection status = new ServiceTransportInspection();
        status.setType( transport.transportType().name() );
        status.setRuntimeIfaceCompileSupported( transport.supportsRuntimeIfaceCompile() );
        status.setControllerSummary( transport.queryControllerSummary() );
        status.setIfaceSummary( transport.queryIfaceSummary() );
        status.setControllerCount( transport.queryRegisteredControllerCount() );
        status.setIfaceCount( transport.queryCompiledIfaceCount() );

        try {
            status.setStarted( transport.isStarted() );
            status.setTerminated( transport.isTerminated() );
            status.setAvailable( status.isStarted() && !status.isTerminated() );
            status.setConnectedClientCount( transport.queryConnectedClientCount() );
        }
        catch ( RuntimeException exception ) {
            status.setAvailable( false );
            status.setLastError( exception.getMessage() );
        }
        return status;
    }

    protected Map<String, Integer> queryInstanceStatusCounts() {
        Map<String, Integer> statusCounts = new LinkedHashMap<>();
        for ( ServiceInstanceStatus status : ServiceInstanceStatus.values() ) {
            statusCounts.put( status.getName(), 0 );
        }

        for ( ClientInstance clientInstance : this.mInstanceRegistry.values() ) {
            if ( clientInstance == null || clientInstance.getInstance() == null ) {
                continue;
            }
            ServiceInstanceEntry entry = this.mServiceInstrument.queryServiceInstance( (GUID) clientInstance.getInstance().getId() );
            if ( entry == null || entry.getStatus() == null ) {
                continue;
            }
            Integer nCount = statusCounts.get( entry.getStatus() );
            statusCounts.put( entry.getStatus(), nCount == null ? 1 : nCount + 1 );
        }
        return statusCounts;
    }

    protected String resolveOverallStatus(
            ServiceControlInspection status,
            boolean bAnyTransportStarted,
            boolean bAllTransportStarted
    ) {
        if ( !status.isServiceInstrumentReady() ) {
            status.setDiagnosticMessage( "ServiceInstrument is not ready." );
            return "Error";
        }
        if ( status.getTransports().isEmpty() ) {
            status.setDiagnosticMessage( "No service control transport is registered." );
            return "Stopped";
        }
        if ( bAllTransportStarted ) {
            return "Running";
        }
        if ( bAnyTransportStarted ) {
            status.setDiagnosticMessage( "Only part of service control transports are started." );
            return "Partial";
        }

        status.setDiagnosticMessage( "Service control transports are not started." );
        return "Stopped";
    }

    @Override
    public void startService() throws ServiceControlRPCException {
        this.settleDetachedServiceInstancesFromStorage();
        for ( ServiceControlTransport transport : this.mTransportRegistry.transports() ) {
            if ( !transport.isStarted() ) {
                transport.startService();
            }
        }
        this.vitalizeRPCSubsystem();
        this.startDetachedObservationSweeper();
    }

    @Override
    public void terminateService() throws IllegalStateException {
        this.stopDetachedObservationSweeper();
        for ( ServiceControlTransport transport : this.mTransportRegistry.transports() ) {
            if ( !transport.isTerminated() ) {
                transport.terminateService();
            }
        }

        for ( ServiceAppointServer appointServer : this.mServerPoolMap.values() ) {
            if ( appointServer != null ) {
                appointServer.close();
            }
        }
    }

    @Override
    public void addRegisterEventHandler( InstanceLifecycleEventHandler handler ) {
        try {
            this.mEventHandlerLock.writeLock().lock();
            this.mRegisterEventHandlers.add( handler );
        }
        finally {
            this.mEventHandlerLock.writeLock().unlock();
        }
    }

    @Override
    public void removeRegisterEventHandler( InstanceLifecycleEventHandler handler ) {
        try {
            this.mEventHandlerLock.readLock().lock();
            this.mRegisterEventHandlers.remove( handler );
        }
        finally {
            this.mEventHandlerLock.readLock().unlock();
        }
    }

    @Override
    public int registerEventHandlerSize() {
        try {
            this.mEventHandlerLock.readLock().lock();
            return this.mRegisterEventHandlers.size();
        }
        finally {
            this.mEventHandlerLock.readLock().unlock();
        }
    }

    protected void triggerServiceEvent(long clientId, Identification insId, InstanceLifecycleEvent event, Object caused ) {
        ServiceInstance instance = this.mCIdInstanceRegistry.get( clientId );
        if ( instance == null ) {
            return;
        }
        GUID serviceId = (GUID) instance.getUSII().getServiceId();

        for ( InstanceLifecycleEventHandler handler : this.mRegisterEventHandlers ) {
            handler.fired( clientId, (GUID) insId, serviceId, event, caused );
        }
    }

    protected void triggerServiceEvent(
            long clientId, Identification insId, GUID serviceId, InstanceLifecycleEvent event, Object caused
    ) {
        for ( InstanceLifecycleEventHandler handler : this.mRegisterEventHandlers ) {
            handler.fired( clientId, (GUID) insId, serviceId, event, caused );
        }
    }



    //    @Override
//    public void registerService( ServiceInstance instance ) {
//        USII primaryKey = instance.getUSII();
//        Long clientId   = primaryKey.getClientId();
//
//        this.mServiceRegistry.compute( primaryKey, ( key, ins ) -> {
//            if ( ins == null ) {
//                ins = new ConcurrentHashMap<>();
//            }
//            ins.put( clientId, instance );
//            return ins;
//        } );
//    }

    @Override
    public void registerServiceInstance( ServiceInstance instance ) {
        Identification primaryKey = instance.getUSII().getServiceId();
        Long clientId   = instance.getUSII().getClientId();

        this.mCIdInstanceRegistry.put( clientId, instance );
        this.mInstanceRegistry.put( instance.getId(), new ClientInstance( clientId, instance ) );

        this.mServiceRegistry.compute( primaryKey, ( key, ins ) -> {
            if ( ins == null ) {
                ins = new ConcurrentHashMap<>();
            }
            ins.put( clientId, instance );
            return ins;
        } );

        this.triggerServiceEvent( clientId, instance.getId(), InstanceLifecycleEvent.Registered, instance );
    }

    @Override
    public GUID registerService( Long clientId, GUID serviceId, GUID deployGuid ) throws ClientServiceRegisterException {
        RegisterServiceDTO serviceDTO = new RegisterServiceDTO( clientId, serviceId.toString(), deployGuid == null ? null : deployGuid.toString() );
        return this.registerService( serviceDTO );
    }

    @Override
    public GUID registerService( RegisterServiceDTO serviceDTO ) throws ClientServiceRegisterException {
        synchronized ( this.mServiceRegistry ) {
            Long clientId = serviceDTO.getClientId();
            GUID serviceId = this.mGuidAllocator.parse( serviceDTO.getServiceId() );
            GUID deployGuid = null;
            if ( serviceDTO.getDeployId() != null && !serviceDTO.getDeployId().isBlank() ) {
                deployGuid = this.mGuidAllocator.parse( serviceDTO.getDeployId() );
            }
            ServiceClientile client = this.mClientRegistry.get( clientId );
            if ( client == null ) {
                throw new ClientServiceRegisterException( "Client " + clientId + " is not existed." );
            }

            SocketAddress remote = client.getRemoteAddress();
            String ip = "";
            if ( remote instanceof InetSocketAddress ) {
                InetSocketAddress inet = (InetSocketAddress) remote;
                ip = this.getInetSocketHost( inet );
            }

            ServiceInstance existing = this.mCIdInstanceRegistry.get( clientId );
            if ( existing != null ) {
                boolean bRecovered = this.mDetachedObservationRegistry.get( clientId ) != null;
                boolean bRestoreRegistration = this.isServiceInstanceRestoreRegistration( existing, serviceDTO );
                GUID existingGuid = this.affirmExistingRegistration(
                        existing,
                        serviceDTO,
                        serviceId,
                        deployGuid,
                        remote
                );
                if ( bRestoreRegistration ) {
                    this.mLogger.info( "Remote serviceInstance {} restore success. <IP:{}>", existingGuid, ip );
                }
                else {
                    this.mLogger.info( "Remote serviceInstance {} register idempotently. <IP:{}>", existingGuid, ip );
                }
                if ( bRecovered ) {
                    this.recoverDetachedServiceInstance(
                            clientId,
                            (GUID) existing.getId(),
                            (GUID) existing.getServiceId(),
                            existing
                    );
                }
                return existingGuid;
            }

            ServiceInstanceEntry resumed = this.resumeServiceInstanceMeta( serviceDTO, serviceId, deployGuid, remote );
            if ( resumed != null ) {
                ServiceInstance serviceInstance = this.createRuntimeServiceInstance( serviceDTO.getClientId(), serviceId, resumed.getGuid() );
                this.registerServiceInstance( serviceInstance );
                this.mDetachedObservationRegistry.remove( clientId );
                this.mLogger.info( "Remote serviceInstance {} resume success. <IP:{}>", resumed.getGuid(), ip );
                return resumed.getGuid();
            }

            ServiceInstanceEntry neo = this.createServiceInstanceMeta( serviceDTO, serviceId, deployGuid, remote ); // new
            ServiceInstanceEntry element = this.updateServiceInstanceStatus( neo.getGuid(), ServiceInstanceStatus.Online );

            ServiceInstance serviceInstance = this.createRuntimeServiceInstance( clientId, serviceId, element.getGuid() );
            this.registerServiceInstance( serviceInstance );
            this.mLogger.info( "Remote serviceInstance {} register success. <IP:{}>", element.getGuid(), ip );

            return element.getGuid();
        }
    }

    protected ServiceInstance createRuntimeServiceInstance( Long clientId, GUID serviceId, GUID instanceId ) {
        TreeNode node = this.mServiceInstrument.get( serviceId );
        ServiceElement serviceElement = (ServiceElement) node;
        return new WolfServiceInstance( clientId, new UniformService( serviceId, serviceElement ), instanceId );
    }

    protected ServiceInstanceEntry resumeServiceInstanceMeta(
            RegisterServiceDTO serviceDTO,
            GUID serviceId,
            GUID deployGuid,
            SocketAddress remote
    ) throws ClientServiceRegisterException {
        String szInstanceId = serviceDTO.getInstanceGuid();
        if ( szInstanceId == null || szInstanceId.isBlank() ) {
            return null;
        }

        GUID instanceId = this.mGuidAllocator.parse( szInstanceId );
        ServiceInstanceEntry entry = this.mServiceInstrument.queryServiceInstance( instanceId );
        if ( entry == null ) {
            return null;
        }

        this.assertSameRegistrationValue( "serviceGuid", entry.getServiceGuid(), serviceId );
        this.assertSameRegistrationValue( "deployGuid", entry.getDeployGuid(), deployGuid );
        this.refreshServiceInstanceRegistration( entry, serviceDTO, remote, true );
        return entry;
    }

    protected GUID affirmExistingRegistration(
            ServiceInstance existing,
            RegisterServiceDTO serviceDTO,
            GUID serviceId,
            GUID deployGuid,
            SocketAddress remote
    ) throws ClientServiceRegisterException {
        if ( !Objects.equals( existing.getServiceId(), serviceId ) ) {
            throw new ClientServiceRegisterException(
                    "Client " + serviceDTO.getClientId() + " has registered another service, existing => `"
                            + existing.getServiceId() + "`, request => `" + serviceId + "`."
            );
        }

        ServiceInstanceEntry entry = this.mServiceInstrument.queryServiceInstance( (GUID) existing.getId() );
        if ( entry == null ) {
            return (GUID) existing.getId();
        }

        if ( this.isServiceInstanceRestoreRegistration( existing, serviceDTO ) ) {
            this.assertSameRegistrationValue( "deployGuid", entry.getDeployGuid(), deployGuid );
            this.refreshServiceInstanceRegistration( entry, serviceDTO, remote, true );
            return entry.getGuid();
        }

        String remoteAddress = remote == null ? "" : remote.toString();
        String endpointHost = "";
        Integer endpointPort = null;
        if ( remote instanceof InetSocketAddress ) {
            InetSocketAddress inet = (InetSocketAddress) remote;
            endpointHost = this.getInetSocketHost( inet );
            endpointPort = inet.getPort();
        }

        String szTransportType = this.notBlankOrDefault( serviceDTO.getTransportType(), ServiceControlTransportType.Husky.name() );
        String szEndpointProtocol = this.notBlankOrDefault( serviceDTO.getEndpointProtocol(), szTransportType );
        String szEndpointHost = this.notBlankOrDefault( serviceDTO.getEndpointHost(), endpointHost );
        Integer nEndpointPort = serviceDTO.getEndpointPort() == null ? endpointPort : serviceDTO.getEndpointPort();
        String szEndpointAddress = this.notBlankOrDefault( serviceDTO.getEndpointAddress(), remoteAddress );

        this.assertSameRegistrationValue( "deployGuid", entry.getDeployGuid(), deployGuid );
        if ( !ServiceInstanceStatus.Online.getName().equals( entry.getStatus() ) ) {
            this.refreshServiceInstanceRegistration( entry, serviceDTO, remote, true );
            return entry.getGuid();
        }
        if ( !Objects.equals( entry.getRemoteAddress(), remoteAddress )
                || !Objects.equals( entry.getEndpointProtocol(), szEndpointProtocol )
                || !Objects.equals( entry.getEndpointHost(), szEndpointHost )
                || !Objects.equals( entry.getEndpointPort(), nEndpointPort )
                || !Objects.equals( entry.getEndpointAddress(), szEndpointAddress ) ) {
            this.refreshServiceInstanceRegistration( entry, serviceDTO, remote, true );
            return entry.getGuid();
        }

        this.assertSameRegistrationValue( "transportType", entry.getTransportType(), szTransportType );
        this.assertSameRegistrationValue( "endpointProtocol", entry.getEndpointProtocol(), szEndpointProtocol );
        this.assertSameRegistrationValue( "endpointHost", entry.getEndpointHost(), szEndpointHost );
        this.assertSameRegistrationValue( "endpointPort", entry.getEndpointPort(), nEndpointPort );
        this.assertSameRegistrationValue( "endpointPath", entry.getEndpointPath(), serviceDTO.getEndpointPath() );
        this.assertSameRegistrationValue( "endpointAddress", entry.getEndpointAddress(), szEndpointAddress );
        this.assertSameRegistrationValue( "version", entry.getVersion(), serviceDTO.getVersion() );
        this.assertSameRegistrationValue( "zone", entry.getZone(), serviceDTO.getZone() );
        if ( serviceDTO.getWeight() != null ) {
            this.assertSameRegistrationValue( "weight", entry.getWeight(), serviceDTO.getWeight() );
        }
        this.assertSameRegistrationValue( "metadataJson", entry.getMetadataJson(), serviceDTO.getMetadataJson() );

        return entry.getGuid();
    }

    protected boolean isServiceInstanceRestoreRegistration( ServiceInstance existing, RegisterServiceDTO serviceDTO ) {
        String szInstanceGuid = serviceDTO.getInstanceGuid();
        if ( szInstanceGuid == null || szInstanceGuid.isBlank() ) {
            return false;
        }

        GUID requestedInstanceGuid = this.mGuidAllocator.parse( szInstanceGuid );
        return Objects.equals( existing.getId(), requestedInstanceGuid );
    }

    protected void refreshServiceInstanceRegistration(
            ServiceInstanceEntry entry,
            RegisterServiceDTO serviceDTO,
            SocketAddress remote,
            boolean bIncreaseConnectionCount
    ) {
        LocalDateTime time = LocalDateTime.now();
        String remoteAddress = remote == null ? "" : remote.toString();
        String endpointHost = "";
        Integer endpointPort = null;
        if ( remote instanceof InetSocketAddress ) {
            InetSocketAddress inet = (InetSocketAddress) remote;
            endpointHost = this.getInetSocketHost( inet );
            endpointPort = inet.getPort();
        }

        String szTransportType = this.notBlankOrDefault( serviceDTO.getTransportType(), ServiceControlTransportType.Husky.name() );
        entry.setClientId( serviceDTO.getClientId() );
        entry.setTransportType( szTransportType );
        entry.setRemoteAddress( remoteAddress );
        entry.setEndpointProtocol( this.notBlankOrDefault( serviceDTO.getEndpointProtocol(), szTransportType ) );
        entry.setEndpointHost( this.notBlankOrDefault( serviceDTO.getEndpointHost(), endpointHost ) );
        entry.setEndpointPort( serviceDTO.getEndpointPort() == null ? endpointPort : serviceDTO.getEndpointPort() );
        entry.setEndpointPath( serviceDTO.getEndpointPath() );
        entry.setEndpointAddress( this.notBlankOrDefault( serviceDTO.getEndpointAddress(), remoteAddress ) );
        entry.setStatus( ServiceInstanceStatus.Online.getName() );
        entry.setStatusReason( null );
        entry.setVersion( serviceDTO.getVersion() );
        entry.setZone( serviceDTO.getZone() );
        if ( serviceDTO.getWeight() != null ) {
            entry.setWeight( serviceDTO.getWeight() );
        }
        entry.setLastHeartbeatTime( time );
        entry.setLatestStartTime( time );
        if ( bIncreaseConnectionCount ) {
            entry.setConnectionCount( entry.getConnectionCount() + 1 );
        }
        entry.setMetadataJson( serviceDTO.getMetadataJson() );
        this.mServiceInstrument.updateServiceInstance( entry );
    }

    protected void assertSameRegistrationValue( String szName, Object existing, Object requested )
            throws ClientServiceRegisterException {
        if ( Objects.equals( existing, requested ) ) {
            return;
        }
        throw new ClientServiceRegisterException(
                "Client registration conflicts on `" + szName + "`, existing => `" + existing
                        + "`, request => `" + requested + "`."
        );
    }

    protected ServiceInstanceEntry updateServiceInstanceStatus( GUID id, ServiceInstanceStatus status ) {
        ServiceInstanceEntry element = this.mServiceInstrument.queryServiceInstance( id );
        if ( element != null ) {
            element.setStatus( status.getName() );
            if ( this.isTerminalInstanceStatus( status ) ) {
                LocalDateTime time = LocalDateTime.now();
                element.setOfflineTime( time );
                element.setLatestEndTime( time );
            }
            this.mServiceInstrument.updateServiceInstance( element );
        }

        return element;
    }

    protected boolean isTerminalInstanceStatus( ServiceInstanceStatus status ) {
        return ServiceInstanceStatus.Offline == status
                || ServiceInstanceStatus.Deregistered == status
                || ServiceInstanceStatus.Expired == status
                || ServiceInstanceStatus.Error == status;
    }

    @Override
    public void markServiceInstanceDetached( Long clientId, Object caused ) {
        if ( clientId == null ) {
            return;
        }
        if ( !this.mDetachedObservationConfig.isEnable() ) {
            this.deregisterServiceInstance( clientId );
            return;
        }

        synchronized ( this.mServiceRegistry ) {
            ServiceInstance instance = this.mCIdInstanceRegistry.get( clientId );
            if ( instance == null ) {
                this.transportRegistry().detachClient( clientId );
                return;
            }

            long nowMillis = System.currentTimeMillis();
            long observationId = this.mDetachedObservationSequence.incrementAndGet();
            GUID instanceGuid = (GUID) instance.getId();
            GUID serviceGuid = (GUID) instance.getServiceId();
            this.mDetachedObservationRegistry.put(
                    new ServiceDetachedObservationEntry(
                            clientId,
                            observationId,
                            instanceGuid,
                            serviceGuid,
                            nowMillis,
                            nowMillis + this.mDetachedObservationConfig.getGraceMillis(),
                            caused
                    )
            );
            this.transportRegistry().detachClient( clientId );
            this.updateServiceInstanceStatus( instanceGuid, ServiceInstanceStatus.Detached );
            this.getLogger().info(
                    "Service instance detached, { clientId: {}, observationId: {}, instanceId: {}, serviceId: {}, graceMillis: {}, deadlineMillis: {} }. <Detached>",
                    new Object[]{
                            clientId,
                            observationId,
                            instanceGuid,
                            serviceGuid,
                            this.mDetachedObservationConfig.getGraceMillis(),
                            nowMillis + this.mDetachedObservationConfig.getGraceMillis()
                    }
            );
            this.triggerServiceEvent( clientId, instanceGuid, serviceGuid, InstanceLifecycleEvent.Detached, caused );
        }
    }

    protected void recoverDetachedServiceInstance( Long clientId, GUID instanceGuid, GUID serviceGuid, Object caused ) {
        ServiceDetachedObservationEntry detached = this.mDetachedObservationRegistry.remove( clientId );
        if ( detached == null ) {
            return;
        }

        this.getLogger().info(
                "Detached service instance recovered, { clientId: {}, observationId: {}, instanceId: {}, serviceId: {} }. <Recovered>",
                new Object[]{ clientId, detached.getObservationId(), instanceGuid, serviceGuid }
        );
        this.triggerServiceEvent( clientId, instanceGuid, serviceGuid, InstanceLifecycleEvent.Recovered, caused );
    }

    protected void settleDetachedServiceInstancesFromStorage() {
        if ( !this.mDetachedObservationConfig.isEnable() ) {
            return;
        }

        long nLastId = 0L;
        int nRecovered = 0;
        int nPageSize = this.mDetachedObservationConfig.getStartupRecoveryPageSize();
        ServiceInstanceStatus status = this.resolveDetachedMissingStatus();

        while ( true ) {
            List<ServiceInstanceEntry> entries = this.mServiceInstrument.fetchServiceInstancesByStatusAfterId(
                    ServiceInstanceStatus.Detached.getName(),
                    nLastId,
                    nPageSize
            );
            if ( entries == null || entries.isEmpty() ) {
                break;
            }

            for ( ServiceInstanceEntry entry : entries ) {
                if ( entry.getId() != null && entry.getId() > nLastId ) {
                    nLastId = entry.getId();
                }
                if ( this.settleDetachedStorageEntry( entry, status ) ) {
                    nRecovered++;
                }
            }

            if ( entries.size() < nPageSize ) {
                break;
            }
        }

        if ( nRecovered > 0 ) {
            this.getLogger().info(
                    "[ServiceControl] [DetachedObservation] (Recovered: `{}`, Status: `{}`) <StorageRecovered>",
                    nRecovered,
                    status.getName()
            );
        }
    }

    protected boolean settleDetachedStorageEntry( ServiceInstanceEntry entry, ServiceInstanceStatus status ) {
        if ( entry == null || entry.getGuid() == null ) {
            return false;
        }
        if ( !ServiceInstanceStatus.Detached.getName().equals( entry.getStatus() ) ) {
            return false;
        }
        if ( this.updateDetachedServiceInstanceStatus( entry.getGuid(), status ) < 1 ) {
            return false;
        }

        this.getLogger().info(
                "Detached service instance settled from storage, { instanceId: {}, serviceId: {}, status: {} }. <{}>",
                new Object[]{
                        entry.getGuid(),
                        entry.getServiceGuid(),
                        status.getName(),
                        status.getName()
                }
        );
        return true;
    }

    protected void settleDetachedServiceInstance( ServiceDetachedObservationEntry entry ) {
        if ( entry == null || entry.getClientId() == null ) {
            return;
        }

        synchronized ( this.mServiceRegistry ) {
            ServiceDetachedObservationEntry current = this.mDetachedObservationRegistry.get( entry.getClientId() );
            if ( current != entry || entry.getDeadlineMillis() > System.currentTimeMillis() ) {
                return;
            }

            ServiceInstance instance = this.mCIdInstanceRegistry.get( entry.getClientId() );
            if ( instance == null || !Objects.equals( instance.getId(), entry.getInstanceGuid() ) ) {
                return;
            }

            ServiceInstanceStatus status = this.resolveDetachedMissingStatus();
            InstanceLifecycleEvent event = this.resolveDetachedMissingEvent( status );
            this.mDetachedObservationRegistry.remove( entry );
            this.removeRuntimeServiceInstance( instance, entry.getClientId(), status );
            this.getLogger().info(
                    "Detached service instance settled, { clientId: {}, observationId: {}, instanceId: {}, serviceId: {}, status: {} }. <{}>",
                    new Object[]{
                            entry.getClientId(),
                            entry.getObservationId(),
                            entry.getInstanceGuid(),
                            entry.getServiceGuid(),
                            status.getName(),
                            status.getName()
                    }
            );
            this.triggerServiceEvent(
                    entry.getClientId(),
                    entry.getInstanceGuid(),
                    entry.getServiceGuid(),
                    event,
                    entry.getCaused()
            );
        }
    }

    protected ServiceInstanceStatus resolveDetachedMissingStatus() {
        String policy = this.mDetachedObservationConfig.getMissingAfterReconnectPolicy();
        String normalizedPolicy = policy == null ? "" : policy.trim().toUpperCase( Locale.ROOT );
        if ( ServiceInstanceStatus.Expired.getName().toUpperCase( Locale.ROOT ).equals( normalizedPolicy ) ) {
            return ServiceInstanceStatus.Expired;
        }
        if ( ServiceInstanceStatus.Offline.getName().toUpperCase( Locale.ROOT ).equals( normalizedPolicy )
                || "OFFLINE".equals( normalizedPolicy ) ) {
            return ServiceInstanceStatus.Offline;
        }

        this.getLogger().warn(
                "Unknown detached missing policy `{}`, fallback to `{}`.",
                policy,
                ServiceInstanceStatus.Offline.getName()
        );
        return ServiceInstanceStatus.Offline;
    }

    protected InstanceLifecycleEvent resolveDetachedMissingEvent( ServiceInstanceStatus status ) {
        if ( ServiceInstanceStatus.Expired == status ) {
            return InstanceLifecycleEvent.Expired;
        }
        return InstanceLifecycleEvent.Offline;
    }

    protected int updateDetachedServiceInstanceStatus( GUID id, ServiceInstanceStatus status ) {
        LocalDateTime terminalTime = this.isTerminalInstanceStatus( status ) ? LocalDateTime.now() : null;
        return this.mServiceInstrument.updateServiceInstanceStatusIfCurrentStatus(
                id,
                ServiceInstanceStatus.Detached.getName(),
                status.getName(),
                terminalTime,
                terminalTime
        );
    }

    protected Collection<ServiceInstance> removeRuntimeServiceInstance(
            ServiceInstance instance,
            Long clientId,
            ServiceInstanceStatus status
    ) {
        if ( instance == null || clientId == null ) {
            return null;
        }

        ServiceInstance eliminated = this.mCIdInstanceRegistry.remove( clientId );
        if ( eliminated == null ) {
            return null;
        }

        ConcurrentMap<Long, ServiceInstance> instances = this.mServiceRegistry.get( eliminated.getServiceId() );
        if ( instances == null ) {
            throw new AssertionFailedException( "Illegal internal statue, mismatched elimination-service size." );
        }

        ServiceInstance removed = instances.remove( clientId );
        if ( removed == null ) {
            return null;
        }

        ClientInstance clientInstance = this.mInstanceRegistry.get( removed.getId() );
        if ( clientInstance != null && Objects.equals( clientInstance.getClientId(), clientId ) ) {
            this.mInstanceRegistry.remove( removed.getId(), clientInstance );
        }

        if ( instances.isEmpty() ) {
            this.mServiceRegistry.remove( removed.getServiceId(), instances );
        }

        this.transportRegistry().detachClient( clientId );
        this.updateServiceInstanceStatus( (GUID) removed.getId(), status );
        return List.of( removed );
    }

    protected void sweepDetachedServiceInstances() {
        if ( !this.mDetachedObservationConfig.isEnable() || this.mDetachedExpirationExecutor == null ) {
            return;
        }

        Collection<ServiceDetachedObservationEntry> expired =
                this.mDetachedObservationRegistry.snapshotExpired( System.currentTimeMillis() );
        for ( ServiceDetachedObservationEntry entry : expired ) {
            this.mDetachedExpirationExecutor.submit( () -> {
                try {
                    this.settleDetachedServiceInstance( entry );
                }
                catch ( Throwable e ) {
                    this.getLogger().warn(
                            "Detached service instance settlement failed, { clientId: {}, observationId: {}, instanceId: {} }. <Failed>",
                            new Object[]{ entry.getClientId(), entry.getObservationId(), entry.getInstanceGuid(), e }
                    );
                }
            } );
        }
    }

    protected void startDetachedObservationSweeper() {
        if ( !this.mDetachedObservationConfig.isEnable() ) {
            return;
        }
        if ( this.mDetachedObservationSweeper != null ) {
            return;
        }

        this.mDetachedExpirationExecutor = Executors.newFixedThreadPool(
                this.mDetachedObservationConfig.getExpireAsyncThreads()
        );
        this.mDetachedObservationSweeper = Executors.newSingleThreadScheduledExecutor( runnable -> {
            Thread thread = new Thread( runnable, "RedQueen-ServiceDetachedObservationSweeper" );
            thread.setDaemon( true );
            return thread;
        } );
        this.mDetachedObservationSweeper.scheduleWithFixedDelay(
                this::sweepDetachedServiceInstances,
                this.mDetachedObservationConfig.getSweepMillis(),
                this.mDetachedObservationConfig.getSweepMillis(),
                TimeUnit.MILLISECONDS
        );
        this.getLogger().info(
                "[ServiceControl] [DetachedObservation] (Enable: `{}`, GraceMillis: `{}`, SweepMillis: `{}`, ExpireAsyncThreads: `{}`, MissingAfterReconnectPolicy: `{}`) <Started>",
                this.mDetachedObservationConfig.isEnable(),
                this.mDetachedObservationConfig.getGraceMillis(),
                this.mDetachedObservationConfig.getSweepMillis(),
                this.mDetachedObservationConfig.getExpireAsyncThreads(),
                this.mDetachedObservationConfig.getMissingAfterReconnectPolicy()
        );
    }

    protected void stopDetachedObservationSweeper() {
        if ( this.mDetachedObservationSweeper != null ) {
            this.mDetachedObservationSweeper.shutdownNow();
            this.mDetachedObservationSweeper = null;
        }
        if ( this.mDetachedExpirationExecutor != null ) {
            this.mDetachedExpirationExecutor.shutdownNow();
            this.mDetachedExpirationExecutor = null;
        }
    }

    @Override
    public void destroyServiceInstance( GUID serviceId, GUID instanceGuid ) {

    }

    @Override
    public Collection<ServiceInstance > fetchServiceInstance( Long clientId ) {
        return List.of( this.mCIdInstanceRegistry.get( clientId ) );
    }

    @Override
    public Collection<ServiceInstance >  fetchServiceInstance( Identification serviceId ) {
        ConcurrentMap<Long, ServiceInstance> map = this.mServiceRegistry.get( serviceId );
        if ( map != null ) {
            return map.values();
        }
        return List.of();
    }

    @Override
    public Collection<ServiceInstance> fetchServiceInstanceByIId( Identification instanceId ) {
        ClientInstance i = this.mInstanceRegistry.get( instanceId );
        if ( i == null ) {
            return List.of();
        }
        return List.of( i.getInstance() );
    }

    @Override
    public Collection<ServiceInstance >  fetchServiceInstance( USII usii ) {
        return this.fetchServiceInstance( usii.getServiceId() );
    }

    @Override
    public ServiceInstance queryServiceInstance( USII usii ) {
        return this.queryServiceInstance( usii.getClientId() );
    }

    @Override
    public ServiceInstance queryServiceInstance( Long clientId ) {
        return this.mCIdInstanceRegistry.get( clientId );
    }

    @Override
    public boolean hasOwnedService( USII usii ) {
        return this.hasOwnedService( usii.getServiceId() );
    }

    @Override
    public boolean hasOwnedService( Identification serviceId ) {
        return this.mServiceRegistry.containsKey( serviceId );
    }

    @Override
    public boolean hasOwnedInstance( Identification instanceId ) {
        return this.mInstanceRegistry.containsKey( instanceId );
    }

    @Override
    public boolean hasOwnedServiceInstance( Long clientId ) {
        return this.mClientRegistry.containsKey( clientId );
    }

    @Override
    public boolean hasOwnedServiceClient( Long clientId ) {
        return this.mClientRegistry.containsKey( clientId );
    }

    @Override
    public ServiceInstance getInstance( Identification instanceId ) {
        ClientInstance i = this.mInstanceRegistry.get( instanceId );
        if ( i == null ) {
            return null;
        }
        return i.getInstance();
    }

    /**
     * Finally elimination inlet function.
     * 终末清除入口点
     */
    @Override
    public Collection<ServiceInstance >  deregisterServiceInstance( Long clientId ) {
        synchronized ( this.mServiceRegistry ) {
            ServiceInstance eliminated = this.mCIdInstanceRegistry.get( clientId );
            ServiceDetachedObservationEntry detached = this.mDetachedObservationRegistry.get( clientId );
            if ( detached != null
                    && eliminated != null
                    && Objects.equals( eliminated.getId(), detached.getInstanceGuid() ) ) {
                this.getLogger().info(
                        "Skip offline detached service instance, { clientId: {}, instanceId: {}, serviceId: {} }. <Detached>",
                        new Object[]{ clientId, eliminated.getId(), eliminated.getServiceId() }
                );
                return List.of();
            }
            Collection<ServiceInstance> removed = this.removeRuntimeServiceInstance(
                    eliminated,
                    clientId,
                    ServiceInstanceStatus.Offline
            );
            this.mDetachedObservationRegistry.remove( clientId );
            if ( removed != null && !removed.isEmpty() ) {
                ServiceInstance serviceInstance = removed.iterator().next();
                this.getLogger().info(
                        "Offline service instance, { clientId: {}, instanceId: {}, serviceId: {} }. <Offline>",
                        new Object[]{ clientId, serviceInstance.getId(), serviceInstance.getServiceId() }
                );

                this.triggerServiceEvent(
                        clientId,
                        serviceInstance.getId(),
                        (GUID) serviceInstance.getServiceId(),
                        InstanceLifecycleEvent.Offline,
                        serviceInstance
                );
            }
            return removed;
        }
    }

    @Override
    public Collection<ServiceInstance > deregisterServiceInstance( Identification instanceId ) {
        ClientInstance clientInstance = this.mInstanceRegistry.get( instanceId );
        if ( clientInstance == null ) {
            return null;
        }

        return this.deregisterServiceInstance( clientInstance.getClientId() );
    }

    @Override
    public Collection<ServiceInstance > deregisterService( Identification serviceId ) {
        ConcurrentMap<Long, ServiceInstance > instances = this.mServiceRegistry.remove( serviceId );
        if ( instances != null ) {
            for ( Map.Entry<Long, ServiceInstance > kv : instances.entrySet() ) {
                this.deregisterServiceInstance( kv.getKey() );
            }
            return instances.values();
        }
        return null;
    }

    @Override
    public void shutdownServiceInstance( Identification instanceId, String szReason ) throws ServiceControlRPCException {
        ClientInstance clientInstance = this.mInstanceRegistry.get( instanceId );
        if ( clientInstance == null ) {
            throw new ServiceControlRPCException( "Service instance is not registered, instanceId => `" + instanceId + "`." );
        }

        ServiceControlTransport transport = this.mTransportRegistry.requireTransport( clientInstance.getClientId() );
        transport.shutdownClientService( clientInstance.getClientId(), (GUID) instanceId, szReason );
    }

    @Override
    public ServiceInstrument getServicesInstrument() {
        return this.mServiceInstrument;
    }

    @Override
    public int countRegisteredService() {
        return this.mServiceRegistry.size();
    }


    protected ServiceInstanceEntry createServiceInstanceMeta(
            RegisterServiceDTO serviceDTO,
            GUID serviceId,
            GUID deployGuid,
            SocketAddress remote
    ) {
        GUID guid = this.mGuidAllocator.nextGUID();
        ServiceInstanceEntry instanceEntity = new GenericServiceInstanceEntity();
        LocalDateTime registerTime = LocalDateTime.now();
        String remoteAddress = "";
        String endpointHost = "";
        Integer endpointPort = null;

        if ( remote != null ) {
            remoteAddress = remote.toString();
        }
        if ( remote instanceof InetSocketAddress ) {
            InetSocketAddress inet = (InetSocketAddress) remote;
            endpointHost = this.getInetSocketHost( inet );
            endpointPort = inet.getPort();
        }

        instanceEntity.setDeployGuid( deployGuid );
        instanceEntity.setStatus( ServiceInstanceStatus.New.getName() );
        instanceEntity.setClientId( serviceDTO.getClientId() );
        String szTransportType = this.notBlankOrDefault( serviceDTO.getTransportType(), ServiceControlTransportType.Husky.name() );
        instanceEntity.setTransportType( szTransportType );
        instanceEntity.setRemoteAddress( remoteAddress );
        instanceEntity.setEndpointProtocol( this.notBlankOrDefault( serviceDTO.getEndpointProtocol(), szTransportType ) );
        instanceEntity.setEndpointHost( this.notBlankOrDefault( serviceDTO.getEndpointHost(), endpointHost ) );
        instanceEntity.setEndpointPort( serviceDTO.getEndpointPort() == null ? endpointPort : serviceDTO.getEndpointPort() );
        instanceEntity.setEndpointPath( serviceDTO.getEndpointPath() );
        instanceEntity.setEndpointAddress( this.notBlankOrDefault( serviceDTO.getEndpointAddress(), remoteAddress ) );
        instanceEntity.setRegisterTime( registerTime );
        instanceEntity.setLastHeartbeatTime( registerTime );
        instanceEntity.setConnectionCount( 1 );
        instanceEntity.setLatestStartTime( registerTime );
        instanceEntity.setGuid( guid );
        instanceEntity.setServiceGuid( serviceId );
        instanceEntity.setVersion( serviceDTO.getVersion() );
        instanceEntity.setZone( serviceDTO.getZone() );
        if ( serviceDTO.getWeight() != null ) {
            instanceEntity.setWeight( serviceDTO.getWeight() );
        }
        instanceEntity.setMetadataJson( serviceDTO.getMetadataJson() );

        this.mServiceInstrument.createServiceInstance( instanceEntity );

        return instanceEntity;
    }

    protected String notBlankOrDefault( String szValue, String szDefault ) {
        if ( szValue == null || szValue.isBlank() ) {
            return szDefault;
        }
        return szValue;
    }

    protected String getInetSocketHost( InetSocketAddress inet ) {
        if ( inet == null ) {
            return "";
        }
        if ( inet.getAddress() != null ) {
            return inet.getAddress().getHostAddress();
        }
        return inet.getHostString();
    }



    protected static class ClientInstance {
        protected Long clientId;
        protected ServiceInstance instance;

        public ClientInstance( Long clientId, ServiceInstance instance ) {
            this.clientId = clientId;
            this.instance = instance;
        }

        public Long getClientId() {
            return this.clientId;
        }

        public ServiceInstance getInstance() {
            return this.instance;
        }
    }

}
