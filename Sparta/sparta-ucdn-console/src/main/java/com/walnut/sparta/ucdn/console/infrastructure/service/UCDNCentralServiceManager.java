package com.walnut.sparta.ucdn.console.infrastructure.service;

import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.ServiceMetaManipulationIface;
import com.pinecone.hydra.service.registry.UniformServiceManager;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.DuplexAppointServer;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNService;
import com.walnut.sparta.ucdn.console.infrastructure.UCDNContentDelivery;

public class UCDNCentralServiceManager implements UCDNServiceManager {
    protected DuplexAppointServer              serviceControlAppointServer;

    protected DuplexAppointClient              serviceRecallAppointClient;

    protected UniformServiceManager            serviceManager;

    protected ServiceInstrument serviceInstrument;

    protected ServiceLifecycleIface            serviceRegistryLifecycleIface;

    protected ServiceMetaManipulationIface     serviceMateIface;

    protected UCDNService                      ucdnService;

    public UCDNCentralServiceManager( UCDNContentDelivery UCDNContentDelivery) {
        this.ucdnService                   = UCDNContentDelivery.getSpartaUCDNService();
        this.serviceInstrument = this.ucdnService.getServiceInstrument();
        this.serviceManager                = ucdnService.getUniformServiceManager();

        this.serviceControlAppointServer   = this.ucdnService.getPrimaryMessageMiddlewareDirector().getWolfKingAppointServer();
        this.serviceRecallAppointClient    = this.ucdnService.getPrimaryMessageMiddlewareDirector().getWolfAppointClient();

        this.serviceRegistryLifecycleIface = this.serviceRecallAppointClient.getIface( ServiceLifecycleIface.class );
        this.serviceMateIface              = this.serviceRecallAppointClient.getIface( ServiceMetaManipulationIface.class );
    }

    @Override
    public DuplexAppointServer getWolfServer() {
        return this.serviceControlAppointServer;
    }

    @Override
    public ServiceInstrument getServiceInstrument() {
        return this.serviceInstrument;
    }

    @Override
    public DuplexAppointClient getDuplexAppointClient() {
        return this.serviceRecallAppointClient;
    }

    @Override
    public ServiceLifecycleIface getLifecycleIface() {
        return this.serviceRegistryLifecycleIface;
    }

    @Override
    public ServiceMetaManipulationIface getMateIface() {
        return this.serviceMateIface;
    }
}
