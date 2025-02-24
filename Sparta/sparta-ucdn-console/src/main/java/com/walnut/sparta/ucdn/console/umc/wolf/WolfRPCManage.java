package com.walnut.sparta.ucdn.console.umc.wolf;

import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.registry.ServiceLifecycleIface;
import com.pinecone.hydra.service.registry.ServiceMetaManipulationIface;
import com.pinecone.hydra.uma.DuplexAppointClient;
import com.pinecone.hydra.uma.wolf.WolvesAppointServer;

public interface WolfRPCManage extends Pinenut {
    WolvesAppointServer getWolfServer();

    ServicesInstrument getServicesInstrument();

    DuplexAppointClient getDuplexAppointClient();

    ServiceLifecycleIface getLifecycleIFace();

    ServiceMetaManipulationIface getMateIFace();
}
