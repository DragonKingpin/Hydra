package com.pinecone.hydra.service.registry;

import com.pinecone.framework.util.id.GuidAllocator;
import com.pinecone.hydra.service.ServicesManager;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.system.ko.KernelObjectConfig;
import com.pinecone.hydra.unit.imperium.ImperialTree;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class UniformServicesManager implements ServicesManager {
    protected ServicesInstrument   mServicesInstrument;

    protected GuidAllocator        mGuidAllocator;

    protected ImperialTree         mImperialTree;

    protected KernelObjectConfig   mServiceConfig;


    protected ConcurrentMap<Long, ServiceControlBlock > mServiceRegistry;

    public UniformServicesManager( ServicesInstrument servicesInstrument ){
        this.mServicesInstrument = servicesInstrument;
        this.mGuidAllocator = this.mServicesInstrument.getGuidAllocator();
        this.mImperialTree = this.mServicesInstrument.getMasterTrieTree();
        this.mServiceConfig = this.mServicesInstrument.getConfig();
        this.mServiceRegistry = new ConcurrentHashMap<>();
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
    public void registryService(Long clientId, ServiceControlBlock serviceControlBlock) {
        this.mServiceRegistry.put( clientId, serviceControlBlock );
    }

    @Override
    public ServiceControlBlock getServiceControlBlock(Long clientId) {
        return this.mServiceRegistry.get( clientId );
    }

    @Override
    public void removeServiceControlBlock(Long clientId) {
        this.mServiceRegistry.remove( clientId );
    }
}
