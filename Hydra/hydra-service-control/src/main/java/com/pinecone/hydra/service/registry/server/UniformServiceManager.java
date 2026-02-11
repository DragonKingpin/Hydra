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
import com.pinecone.hydra.service.registry.ServiceControlRPCException;
import com.pinecone.hydra.service.registry.UniformService;
import com.pinecone.hydra.service.registry.WolfServiceInstance;
import com.pinecone.hydra.service.registry.appoint.RegisteredServiceClient;
import com.pinecone.hydra.service.registry.appoint.ServiceAppointServer;
import com.pinecone.hydra.service.registry.constant.ServiceStatus;
import com.pinecone.hydra.service.registry.event.ServiceRegisterEvent;
import com.pinecone.hydra.service.registry.event.ServiceRegisterEventHandler;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.unit.imperium.entity.TreeNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

public class UniformServiceManager implements ServiceManager {
    protected final ServiceInstrument                                                       mServiceInstrument;

    protected final ConcurrentMap<Long, ServiceAppointServer >                              mServerPoolMap;  // ServerId => Node
    protected final ConcurrentMap<Long, ServiceInstance >                                   mCIdInstanceRegistry; // ClientId => Instance
    protected final ConcurrentMap<Identification, ConcurrentMap<Long, ServiceInstance> >    mServiceRegistry;  // ServiceId => <CId, Instance>
    protected final ConcurrentMap<Identification, ClientInstance >                          mInstanceRegistry; // InstanceId => Instance
    protected final ConcurrentMap<Long, RegisteredServiceClient>                            mClientRegistry; // ClientId => Client

    protected final List<ServiceRegisterEventHandler>                                       mRegisterEventHandlers;
    protected final GuidAllocator                                                           mGuidAllocator;
    protected final ServiceEventHooker                                                      mServiceEventHooker;

    protected final ServiceLifecycleService                                                 mServiceLifecycleService;
    protected final ServiceMetaService                                                      mServiceMetaService;

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
    public ServiceLifecycleService serviceLifecycleService() {
        return this.mServiceLifecycleService;
    }

    @Override
    public ServiceMetaService getServiceMetaService() {
        return this.mServiceMetaService;
    }

    @Override
    public void startService() throws ServiceControlRPCException {
        this.vitalizeRPCSubsystem();
    }

    @Override
    public void addRegisterEventHandler( ServiceRegisterEventHandler handler ) {
        try {
            this.mEventHandlerLock.writeLock().lock();
            this.mRegisterEventHandlers.add( handler );
        }
        finally {
            this.mEventHandlerLock.writeLock().unlock();
        }
    }

    @Override
    public void removeRegisterEventHandler( ServiceRegisterEventHandler handler ) {
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

    protected void triggerServiceEvent( long clientId, Identification insId, ServiceRegisterEvent event, Object caused ) {
        ServiceInstance instance = this.mCIdInstanceRegistry.get( clientId );
        if ( instance == null ) {
            return;
        }
        GUID serviceId = (GUID) instance.getUSII().getServiceId();

        for ( ServiceRegisterEventHandler handler : this.mRegisterEventHandlers ) {
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

        this.triggerServiceEvent( clientId, instance.getId(), ServiceRegisterEvent.Registered, instance );
    }

    @Override
    public GUID registerService( Long clientId, GUID serviceId, GUID deployGuid ) throws ClientServiceRegisterException {
        synchronized ( this.mServiceRegistry ) {
            RegisteredServiceClient client = this.mClientRegistry.get( clientId );
            if ( client == null ) {
                throw new ClientServiceRegisterException( "Client " + clientId + " is not existed." );
            }

            SocketAddress remote = client.getRemoteAddress();
            String ip = "";
            if ( remote instanceof InetSocketAddress) {
                InetSocketAddress inet = (InetSocketAddress) remote;
                ip  = inet.getAddress().getHostAddress();
            }

            ServiceInstanceEntry neo = this.createServiceInstanceMeta( serviceId, deployGuid, ip ); // new
            ServiceInstanceEntry element = this.updateServiceInstanceStatus( neo.getGuid(), ServiceStatus.SERVICE_RUNNING );

            TreeNode node = this.mServiceInstrument.get( serviceId );
            ServiceElement serviceElement = (ServiceElement) node;
            ServiceInstance serviceInstance = new WolfServiceInstance( clientId, new UniformService( serviceId, serviceElement ), element.getGuid() );
            this.registerServiceInstance( serviceInstance );
            this.mLogger.info( "Remote serviceInstance {} register success. <IP:{}>", element.getGuid(), ip );

            return element.getGuid();
        }
    }

    protected ServiceInstanceEntry updateServiceInstanceStatus( GUID id, ServiceStatus status ) {
        ServiceInstanceEntry element = this.mServiceInstrument.queryServiceInstance( id );
        if ( element != null ) {
            element.setStatus( status.getName() );
            element.setRunCount( element.getRunCount() + 1 );
            this.mServiceInstrument.updateServiceInstance( element );
        }

        return element;
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
            ServiceInstance eliminated = this.mCIdInstanceRegistry.remove( clientId );
            // It’s not thread-safe beyond this critical zone, as the size may be mutated by other threads after this point.
            // 该临界区后面线程并不安全, size 可能在该临界区后被其他线程破坏.
            if ( eliminated != null ) {
                ConcurrentMap<Long, ServiceInstance > instances = this.mServiceRegistry.get( eliminated.getServiceId() );
                if ( instances != null ) {
                    this.mInstanceRegistry.remove( eliminated.getId() );
                    this.updateServiceInstanceStatus( (GUID) eliminated.getId(), ServiceStatus.SERVICE_TERMINATED );
                    this.getLogger().info(
                            "Detached service instance, { clientId: {}, instanceId: {}, serviceId: {} }. <Detached>",
                            clientId, eliminated.getId(), eliminated.getServiceId()
                    );

                    if ( instances.size() <= 1 ) {
                        instances = this.mServiceRegistry.remove( eliminated.getServiceId() );
                        return instances.values();
                    }
                    else {
                        // 副本实例，不用额外变更状态
                        ServiceInstance instance = instances.remove( clientId );
                        if ( instance != null ) {
                            return List.of( instance );
                        }
                    }
                }
                else {
                    throw new AssertionFailedException( "Illegal internal statue, mismatched elimination-service size." );
                }

                this.triggerServiceEvent( clientId, eliminated.getId(), ServiceRegisterEvent.Deregistered, eliminated );
            }
            return null;
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
    public ServiceInstrument getServicesInstrument() {
        return this.mServiceInstrument;
    }

    @Override
    public int countRegisteredService() {
        return this.mServiceRegistry.size();
    }


    protected ServiceInstanceEntry createServiceInstanceMeta( GUID serviceId, GUID deployGuid, String ip ) {
        GUID guid = this.mGuidAllocator.nextGUID();
        ServiceInstanceEntry instanceEntity = new GenericServiceInstanceEntity();

        instanceEntity.setDeployGuid( deployGuid );
        instanceEntity.setStatus( ServiceStatus.SERVICE_NEW.getName() );
        instanceEntity.setLatestStartTime( LocalDateTime.now() );
        instanceEntity.setIp( ip );
        instanceEntity.setGuid( guid );
        instanceEntity.setServiceGuid( serviceId );

        this.mServiceInstrument.createServiceInstance( instanceEntity );

        return instanceEntity;
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
