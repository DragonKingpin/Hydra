package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.framework.util.id.Identification;
import com.pinecone.hydra.service.ServiceInstance;
import com.pinecone.hydra.service.ServiceManager;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.entity.USII;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.unit.imperium.ImperialTree;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UniformServiceManager implements ServiceManager {
    protected ServicesInstrument      mServicesInstrument;

    protected ServiceLifecycleIface   mServiceLifecycleIface;

    protected GuidAllocator           mGuidAllocator;

    protected ImperialTree            mImperialTree;

    protected KernelObjectConfig      mServiceConfig;


    protected ConcurrentMap<USII, ServiceInstance > mServiceRegistry;

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
        this.mServiceRegistry.put( instance.getUSII(), instance );
    }

    @Override
    public ServiceInstance queryServiceInstance( Long clientId ) {
        return this.mServiceRegistry.get( clientId );
    }

    @Override
    public ServiceInstance queryServiceInstance( Identification serviceId ) {
        return this.mServiceRegistry.get( serviceId );
    }

    @Override
    public ServiceInstance queryServiceInstance( USII usii ) {
        return this.mServiceRegistry.get( usii );
    }

    @Override
    public ServiceInstance removeService( Long clientId ) {
        return this.mServiceRegistry.remove( clientId );
    }

    @Override
    public ServiceInstance removeService( Identification serviceId ) {
        return this.mServiceRegistry.remove( serviceId );
    }

    @Override
    public ServiceInstance removeService( USII usii ) {
        return this.mServiceRegistry.remove( usii );
    }
}
