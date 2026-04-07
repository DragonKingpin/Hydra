package com.pinecone.hydra.service.kom.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.unit.imperium.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.imperium.source.TrieTreeManipulator;

public interface ServiceMasterManipulator extends KOIMasterManipulator {
    TrieTreeManipulator getTrieTreeManipulator() ;

    NodeMetaManipulator getNodeMetaManipulator();

    ApplicationNodeManipulator getApplicationNodeManipulator();

    ApplicationMetaManipulator getApplicationElementManipulator();

    ServiceNodeManipulator getServiceNodeManipulator();

    ServiceMetaManipulator getServiceMetaManipulator();

    ServiceNamespaceManipulator getNamespaceManipulator();

    ServiceInstanceManipulator getServiceInstanceManipulator();

    NamespaceRulesManipulator getNamespaceRulesManipulator();

    TireOwnerManipulator getTireOwnerManipulator();

}
