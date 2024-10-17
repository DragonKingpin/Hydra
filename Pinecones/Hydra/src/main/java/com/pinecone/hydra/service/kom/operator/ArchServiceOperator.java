package com.pinecone.hydra.service.kom.operator;

import com.pinecone.hydra.registry.operator.RegistryOperatorFactory;
import com.pinecone.hydra.service.kom.ServicesTree;
import com.pinecone.hydra.service.kom.source.ServiceAttributeManipulator;
import com.pinecone.hydra.service.kom.source.ServiceMasterManipulator;
import com.pinecone.hydra.unit.udtt.DistributedTrieTree;

public abstract class ArchServiceOperator implements ServiceOperator{
    protected ServicesTree                  servicesTree;
    protected DistributedTrieTree           distributedTrieTree;
    protected ServiceAttributeManipulator   attributeManipulator;
    protected ServiceMasterManipulator      serviceMasterManipulator;
    protected ServiceOperatorFactory        factory;

    public ArchServiceOperator( ServiceOperatorFactory factory ){
        this( factory.getServiceMasterManipulator(),factory.getServicesTree() );
        this.factory = factory;
    }
    public ArchServiceOperator( ServiceMasterManipulator masterManipulator, ServicesTree servicesTree ){
        this.distributedTrieTree = servicesTree.getMasterTrieTree();
        this.servicesTree = servicesTree;
        this.attributeManipulator = masterManipulator.getAttributeManipulator();
        this.serviceMasterManipulator = masterManipulator;
        //this.factory = new GenericServiceOperatorFactory(servicesTree,masterManipulator);
    }

    public ServiceOperatorFactory getOperatorFactory() {
        return this.factory;
    }

}
