package com.walnut.sparta.ucdn.console.infrastructure.service;

import com.pinecone.framework.system.regime.arch.Manager;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.ServiceMetaManipulationIface;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.DuplexAppointServer;

public interface UCDNServiceManager extends Manager {
    DuplexAppointServer getWolfServer();

    ServiceInstrument getServiceInstrument();

    DuplexAppointClient getDuplexAppointClient();

    ServiceLifecycleIface getLifecycleIface();

    ServiceMetaManipulationIface getMateIface();
}
