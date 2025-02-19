package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.unit.imperium.ImperialTree;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UniformServiceManager implements ServiceManager {
    protected ServicesInstrument      mServicesInstrument;

    protected ServiceLifecycleIface   mServiceLifecycleIface;

    protected GuidAllocator           mGuidAllocator;

    protected ImperialTree            mImperialTree;

    protected KernelObjectConfig      mServiceConfig;


    protected ConcurrentMap<USII, ConcurrentHashMap<Long, ServiceInstance > > mServiceRegistry;

    public UniformServiceManager( ServicesInstrument servicesInstrument ){
        this.mServicesInstrument = servicesInstrument;
        this.mGuidAllocator      = this.mServicesInstrument.getGuidAllocator();
        this.mImperialTree       = this.mServicesInstrument.getMasterTrieTree();
        this.mServiceConfig      = this.mServicesInstrument.getConfig();
        this.mServiceRegistry    = new ConcurrentHashMap<>();
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


    @Override
    public void registerService( ServiceInstance instance ) {
        USII primaryKey = instance.getUSII();
        ConcurrentHashMap<Long, ServiceInstance > ins = this.mServiceRegistry.computeIfAbsent( primaryKey, ( key )->{
            return new ConcurrentHashMap<>();
        } );
        ins.put( primaryKey.getClientId(), instance );
    }

    @Override
    public Collection<ServiceInstance > queryServiceInstance( Long clientId ) {
        return this.mServiceRegistry.get( clientId ).values();
    }

    @Override
    public Collection<ServiceInstance >  queryServiceInstance( Identification serviceId ) {
        return this.mServiceRegistry.get( serviceId ).values();
    }

    @Override
    public Collection<ServiceInstance >  queryServiceInstance( USII usii ) {
        return this.mServiceRegistry.get( usii ).values();
    }

    @Override
    public Collection<ServiceInstance >  removeService( Long clientId ) {
        ConcurrentHashMap<Long, ServiceInstance > instances = this.mServiceRegistry.get( clientId );
        if ( instances != null ) {
            ServiceInstance instance = instances.remove( clientId );
            if ( instance != null ) {
                return List.of( instance );
            }
        }
        return null;
    }

    @Override
    public Collection<ServiceInstance >  removeService( Identification serviceId ) {
        ConcurrentHashMap<Long, ServiceInstance > instances = this.mServiceRegistry.remove( serviceId );
        if ( instances != null ) {
            return instances.values();
        }
        return null;
    }

    @Override
    public Collection<ServiceInstance >  removeService( USII usii ) {
        ConcurrentHashMap<Long, ServiceInstance > instances = this.mServiceRegistry.remove( usii );
        if ( instances != null ) {
            return instances.values();
        }
        return null;
    }
}
