package com.acorn.redqueen.system;

import com.acorn.redqueen.service.conduct.CollectiveServiceRegiment;
import com.pinecone.framework.system.SynergicSystem;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.inspection.ServiceControlInspection;
import com.pinecone.hydra.system.centrum.CentralControlSubsystem;

public interface ServiceCentralControl extends SynergicSystem, CentralControlSubsystem {

    ServiceInstrument serviceInstrument();

    ServiceManager serviceManager();

    CollectiveServiceRegiment serviceRegiment();

    ServiceControlInspection inspectServiceControl();
}

