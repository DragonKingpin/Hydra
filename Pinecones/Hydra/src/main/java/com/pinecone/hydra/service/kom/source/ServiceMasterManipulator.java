package com.pinecone.hydra.service.kom.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;

public interface ServiceMasterManipulator extends KOIMasterManipulator {
    TrieTreeManipulator getTrieTreeManipulator() ;

    ServiceAttributeManipulator getAttributeManipulator();

    ApplicationNodeManipulator getApplicationNodeManipulator();

    ApplicationMetaManipulator getApplicationMetaManipulator();

    ServiceNodeManipulator getServiceNodeManipulator();

    ServiceMetaManipulator getServiceMetaManipulator();

    ServiceNamespaceManipulator getNamespaceManipulator();

    NamespaceRulesManipulator getNamespaceRulesManipulator();

    ServiceFamilyTreeManipulator getServiceFamilyTreeManipulator();
    TireOwnerManipulator getTireOwnerManipulator();

}
