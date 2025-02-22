package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.pinecone.hydra.unit.imperium.ImperialTree;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UniformServiceManager implements ServiceManager {
    protected ServicesInstrument            mServicesInstrument;

    protected DuplexAppointServer           mAppointServer;

    protected GuidAllocator                 mGuidAllocator;

    protected ImperialTree                  mImperialTree;

    protected KernelObjectConfig            mServiceConfig;


    protected final ConcurrentMap<Long, ServiceInstance > mInstanceRegistry;

    protected final ConcurrentMap<Identification, ConcurrentMap<Long, ServiceInstance> > mServiceRegistry;


    protected void initRPCSubsystem() {
        this.mAppointServer.registerController( new ServiceLifecycleController( this ) );
        this.mAppointServer.registerController( new ServiceMetaController( this ) );
    }

    public UniformServiceManager( ServicesInstrument servicesInstrument, DuplexAppointServer server ){
        this.mServicesInstrument = servicesInstrument;
        this.mGuidAllocator      = this.mServicesInstrument.getGuidAllocator();
        this.mImperialTree       = this.mServicesInstrument.getMasterTrieTree();
        this.mServiceConfig      = this.mServicesInstrument.getConfig();
        this.mAppointServer      = server;
        this.mServiceRegistry    = new ConcurrentHashMap<>();
        this.mInstanceRegistry   = new ConcurrentHashMap<>();

        this.initRPCSubsystem();
    }




    @Override
    public GuidAllocator getGuidAllocator() {
        return this.mGuidAllocator;
    }

    @Override
    public ImperialTree getMasterTrieTree() {
        return this.mImperialTree;
    }

    @Override
    public KernelObjectConfig getConfig() {
        return this.mServiceConfig;
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
    }

    @Override
    public Collection<ServiceInstance > queryServiceInstance( Long clientId ) {
        return List.of( this.mInstanceRegistry.get( clientId ) );
    }

    @Override
    public Collection<ServiceInstance >  queryServiceInstance( Identification serviceId ) {
        return this.mServiceRegistry.get( serviceId ).values();
    }

    @Override
    public Collection<ServiceInstance >  queryServiceInstance( USII usii ) {
        return this.mServiceRegistry.get( usii.getServiceId() ).values();
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
    public ServicesInstrument getServicesInstrument() {
        return this.mServicesInstrument;
    }
}
