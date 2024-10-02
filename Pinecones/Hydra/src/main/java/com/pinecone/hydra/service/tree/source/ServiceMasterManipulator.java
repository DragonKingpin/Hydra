package com.pinecone.hydra.service.tree.source;

import com.pinecone.hydra.system.ko.driver.KOIMasterManipulator;
import com.pinecone.hydra.unit.udtt.source.TireOwnerManipulator;
import com.pinecone.hydra.unit.udtt.source.TrieTreeManipulator;

public interface ServiceMasterManipulator extends KOIMasterManipulator {
    TrieTreeManipulator getTrieTreeManipulator() ;

    CommonDataManipulator getCommonDataManipulator();

    ApplicationNodeManipulator getApplicationNodeManipulator();

    ApplicationMetaManipulator getApplicationMetaManipulator();

    ServiceNodeManipulator getServiceNodeManipulator();

    ServiceMetaManipulator getServiceMetaManipulator();

    ClassifNodeManipulator getClassifNodeManipulator();

    ClassifRulesManipulator getClassifRulesManipulator();

    ServiceFamilyTreeManipulator getServiceFamilyTreeManipulator();
    TireOwnerManipulator getTireOwnerManipulator();

}
