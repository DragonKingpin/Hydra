package com.pinecone.hydra.service.kom.operator;

import com.pinecone.hydra.service.kom.ServicesInstrument;
import com.pinecone.hydra.service.kom.source.CommonDataManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;

public abstract class ArchServiceOperator implements ServiceOperator{
    protected ServicesInstrument            servicesInstrument;
    protected DistributedTrieTree           distributedTrieTree;
    protected CommonDataManipulator         commonDataManipulator;
    protected ServiceMasterManipulator      serviceMasterManipulator;
    protected ServiceOperatorFactory        factory;

    public ArchServiceOperator( ServiceOperatorFactory factory ){
        this( factory.getServiceMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }
    public ArchServiceOperator( ServiceMasterManipulator masterManipulator, ServicesInstrument servicesInstrument){
        this.distributedTrieTree = servicesInstrument.getMasterTrieTree();
        this.servicesInstrument = servicesInstrument;
        this.commonDataManipulator = masterManipulator.getCommonDataManipulator();
        this.serviceMasterManipulator = masterManipulator;
        //this.factory = new GenericServiceOperatorFactory(servicesTree,masterManipulator);
    }

    public ServiceOperatorFactory getOperatorFactory() {
        return this.factory;
    }

}
