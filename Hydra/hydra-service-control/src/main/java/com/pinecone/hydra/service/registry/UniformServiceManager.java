package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.GUID;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.service.registry.event.ServiceRegisterEvent;
import com.pinecone.hydra.service.registry.event.ServiceRegisterEventHandler;
import com.pinecone.hydra.system.component.LogStatuses;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.umc.msg.ChannelControlBlock;
import com.pinecone.hydra.umc.msg.ChannelHandleException;
import com.pinecone.hydra.umc.msg.MessageNode;
import com.pinecone.hydra.umc.msg.event.ChannelEventHandler;
import com.pinecone.hydra.umc.msg.event.ChannelInactiveHandler;
import com.pinecone.hydra.umc.wolf.server.UlfServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UniformServiceManager implements ServiceManager {
    protected ServiceInstrument                                                             mServiceInstrument;

    protected DuplexAppointServer                                                           mAppointServer;

    protected final ConcurrentMap<Long, ServiceInstance >                                   mInstanceRegistry;

    protected final ConcurrentMap<Identification, ConcurrentMap<Long, ServiceInstance> >    mServiceRegistry;

    protected final ConcurrentMap<Long, ConcurrentMap<Object, Object > >                    mClientRegistry;

    protected List<ServiceRegisterEventHandler>                                             mRegisterEventHandlers;

    private static final Object PRESENT = new Object();

    private final Logger mLogger;

    private final ReadWriteLock mEventHandlerLock = new ReentrantReadWriteLock();

    @Override
    public Logger getLogger() {
        return this.mLogger;
    }

    protected void initRPCSubsystem() {
        this.mAppointServer.registerController( new ServiceLifecycleController( this ) );
        this.mAppointServer.registerController( new ServiceMetaController( this ) );

        MessageNode messageNode = this.mAppointServer.getMessageNode();
        UlfServer   ulfServer   = (UlfServer) messageNode;
        ulfServer.registerDataArrivedEventHandlers(new ChannelEventHandler() {
            @Override
            public void afterEventTriggered( ChannelControlBlock block ) {
                long clientId    = block.getChannel().getIdentityID();
                Object channelId = block.getChannel().getChannelID();
                UniformServiceManager.this.mClientRegistry.compute( clientId, ( key, ins ) -> {
                    if ( ins == null ) {
                        ins = new ConcurrentHashMap<>();
                    }
                    ins.put( channelId, PRESENT );
                    return ins;
                } );
            }
        });

        ulfServer.registerChannelInactiveHandler(new ChannelInactiveHandler() {
            @Override
            public boolean afterChannelInactive( ChannelControlBlock ccb ) throws ChannelHandleException {
                Long clientId    = ccb.getChannel().getIdentityID();
                Object channelId = ccb.getChannel().getChannelID();

                UniformServiceManager.this.afterChannelDetach( clientId, channelId );
                return false;
            }
        });
    }

    protected void vitalizeRPCSubsystem() throws ServiceControlRPCException {
        try {
            this.mAppointServer.execute();
            this.infoLifecycle( "RPC Subsystem Service Vitalization", LogStatuses.StatusDone );
        }
        catch ( Exception e ) {
            throw new ServiceControlRPCException( e );
        }
    }

    protected void afterChannelDetach( Long clientId, Object channelId ) {
        synchronized ( this.mClientRegistry ) {
            ConcurrentMap<Object, Object > channelSet = this.mClientRegistry.get( clientId );
            // It’s not thread-safe beyond this critical zone, as the size may be mutated by other threads after this point.
            // 该临界区后面线程并不安全, size 可能在该临界区后被其他线程破坏.
            if ( channelSet != null ) {
                if ( channelSet.size() > 1 ) {
                    channelSet.remove( channelId );
                }
                else {
                    this.mClientRegistry.remove( clientId );
                    this.removeService( clientId );
                }
            }
        }
    }

    public UniformServiceManager( ServiceInstrument serviceInstrument, DuplexAppointServer server ){
        this.mServiceInstrument     = serviceInstrument;
        this.mAppointServer         = server;
        this.mServiceRegistry       = new ConcurrentHashMap<>();
        this.mInstanceRegistry      = new ConcurrentHashMap<>();
        this.mClientRegistry        = new ConcurrentHashMap<>();
        this.mLogger                = LoggerFactory.getLogger( this.getClass() );
        this.mRegisterEventHandlers = new ArrayList<>();
    }

    @Override
    public void startService() throws ServiceControlRPCException {
        this.initRPCSubsystem();
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

    protected void triggerServiceEvent(long clientId, Identification insId, ServiceRegisterEvent event, Object caused ) {
        ServiceInstance instance = this.mInstanceRegistry.get( clientId );
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
    public void registerService( ServiceInstance instance ) {
        Identification primaryKey = instance.getUSII().getServiceId();
        Long clientId   = instance.getUSII().getClientId();

        this.mInstanceRegistry.put( clientId, instance );

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
    public void destroyServiceInstance( GUID serviceId, GUID instanceGuid ) {

    }

    @Override
    public Collection<ServiceInstance > fetchServiceInstance( Long clientId ) {
        return List.of( this.mInstanceRegistry.get( clientId ) );
    }

    @Override
    public Collection<ServiceInstance >  fetchServiceInstance( Identification serviceId ) {
        return this.mServiceRegistry.get( serviceId ).values();
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
        return this.mInstanceRegistry.get( clientId );
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
    public boolean hasOwnedServiceInstance( Long clientId ) {
        return this.mClientRegistry.containsKey( clientId );
    }

    @Override
    public boolean hasOwnedServiceClient( Long clientId ) {
        return this.mClientRegistry.containsKey( clientId );
    }

    @Override
    public Collection<ServiceInstance >  removeService( Long clientId ) {
        synchronized ( this.mServiceRegistry ) {
            ServiceInstance eliminated = this.mInstanceRegistry.remove( clientId );
            // It’s not thread-safe beyond this critical zone, as the size may be mutated by other threads after this point.
            // 该临界区后面线程并不安全, size 可能在该临界区后被其他线程破坏.
            if ( eliminated != null ) {
                ConcurrentMap<Long, ServiceInstance > instances = this.mServiceRegistry.get( eliminated.getId() );
                if ( instances != null ) {
                    if ( instances.size() > 1 ) {
                        ServiceInstance instance = instances.remove( clientId );
                        if ( instance != null ) {
                            return List.of( instance );
                        }
                    }
                    else {
                        ConcurrentMap<Long, ServiceInstance > del = this.mServiceRegistry.remove( eliminated.getId() );
                        if ( del != null ) {
                            return del.values();
                        }
                    }
                }
            }
            return null;
        }
    }

    @Override
    public Collection<ServiceInstance >  removeService( Identification serviceId ) {
        ConcurrentMap<Long, ServiceInstance > instances = this.mServiceRegistry.remove( serviceId );
        if ( instances != null ) {
            return instances.values();
        }
        return null;
    }

    @Override
    public Collection<ServiceInstance >  removeService( USII usii ) {
        ConcurrentMap<Long, ServiceInstance > instances = this.mServiceRegistry.remove( usii );
        if ( instances != null ) {
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
}
