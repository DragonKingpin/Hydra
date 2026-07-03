package com.acorn.redqueen.system;

import com.acorn.redqueen.service.conduct.CollectiveServiceRegiment;
import com.acorn.redqueen.service.purge.PurgeService;
import com.pinecone.hydra.service.kom.ServiceInstrument;
import com.pinecone.hydra.service.registry.server.ServiceManager;
import com.pinecone.hydra.service.registry.server.inspection.ServiceControlInspection;
import com.pinecone.hydra.system.imperium.FacilityClass;
import com.pinecone.hydra.system.imperium.FacilitySynergicSystem;

public interface ServiceCentralControl extends FacilitySynergicSystem {

    ServiceInstrument serviceInstrument();

    ServiceManager serviceManager();

    CollectiveServiceRegiment serviceRegiment();

    ServiceControlInspection inspectServiceControl();

    PurgeService servicePurgeService();

    @Override
    default FacilityClass facilityClass() {
        return FacilityClass.Service;
    }
}
