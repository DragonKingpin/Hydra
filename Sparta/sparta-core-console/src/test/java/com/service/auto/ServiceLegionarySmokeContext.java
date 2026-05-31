package com.service.auto;

import com.acorn.redqueen.service.conduct.RedCollectiveServiceLegionary;
import com.acorn.redqueen.service.conduct.RedCollectiveServiceRegiment;
import com.pinecone.framework.system.prototype.Pinenut;
import com.pinecone.hydra.service.kom.UniformServiceInstrument;
import com.pinecone.hydra.service.registry.client.UniformServiceClient;
import com.pinecone.hydra.service.registry.dto.ServiceMetaDTO;
import com.pinecone.hydra.service.registry.server.UniformServiceManager;
import com.pinecone.tritium.Tritium;

public class ServiceLegionarySmokeContext implements Pinenut {

    public Tritium system;

    public ServiceLegionaryTransportScenario scenario;

    public UniformServiceInstrument serviceInstrument;

    public UniformServiceManager serviceManager;

    public RedCollectiveServiceRegiment regiment;

    public UniformServiceClient serviceClient;

    public RedCollectiveServiceLegionary legionary;

    public ServiceMetaDTO serviceMeta;

    public ServiceLegionaryLifecycleProbe probe;

    public ServiceLegionarySmokeContext( Tritium system, ServiceLegionaryTransportScenario scenario ) {
        this.system = system;
        this.scenario = scenario;
        this.probe = new ServiceLegionaryLifecycleProbe( scenario.name() );
    }

    public void cleanup() {
        try {
            if ( this.legionary != null ) {
                this.legionary.terminateService();
            }
        }
        catch ( Exception ignore ) {
        }
        try {
            if ( this.serviceClient != null ) {
                this.serviceClient.terminateService();
            }
        }
        catch ( Exception ignore ) {
        }
        try {
            if ( this.regiment != null ) {
                this.regiment.stopServiceManage();
            }
        }
        catch ( Exception ignore ) {
        }
        try {
            if ( this.scenario != null ) {
                this.scenario.cleanup( this );
            }
        }
        catch ( Exception ignore ) {
        }
    }
}
